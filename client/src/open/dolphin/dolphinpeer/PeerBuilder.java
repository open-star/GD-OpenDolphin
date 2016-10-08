/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.dolphinpeer;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import open.dolphin.infomodel.AddressModel;
import open.dolphin.infomodel.BeanUtils;
import open.dolphin.infomodel.HealthInsuranceModel;
import open.dolphin.infomodel.PVTClaim;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.infomodel.PVTPublicInsuranceItemModel;
import open.dolphin.infomodel.SimpleAddressModel;
import open.dolphin.infomodel.TelephoneModel;

import open.dolphin.log.LogWriter;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.Namespace;

/**
 * PVTBuilder
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class PeerBuilder {
    private static final Namespace mmlCm = Namespace.getNamespace("mmlCm", "http://www.medxml.net/MML/SharedComponent/Common/1.0");
    private static final Namespace mmlNm = Namespace.getNamespace("mmlNm", "http://www.medxml.net/MML/SharedComponent/Name/1.0");
    private static final Namespace mmlFc = Namespace.getNamespace("mmlFc", "http://www.medxml.net/MML/SharedComponent/Facility/1.0");
    private static final Namespace mmlDp = Namespace.getNamespace("mmlDp", "http://www.medxml.net/MML/SharedComponent/Department/1.0");
    private static final Namespace mmlPsi = Namespace.getNamespace("mmlPsi", "http://www.medxml.net/MML/SharedComponent/PersonalizedInfo/1.0");
    private static final Namespace mmlCi = Namespace.getNamespace("mmlCi", "http://www.medxml.net/MML/SharedComponent/CreatorInfo/1.0");
    private static final Namespace mmlHi = Namespace.getNamespace("mmlHi", "http://www.medxml.net/MML/ContentModule/HealthInsurance/1.1");
    private static final Namespace claim = Namespace.getNamespace("claim", "http://www.medxml.net/claim/claimModule/2.1");
    private boolean DERBY;
    private PatientModel patientModel;
    private AddressModel curAddress;
    private TelephoneModel curTelephone;
    private List<PVTHealthInsuranceModel> pvtInsurnaces;
    private PVTHealthInsuranceModel curInsurance;
    private PVTPublicInsuranceItemModel curPublicItem;
    private PVTClaim pvtClaim;
    private String curRepCode;

    /**
     * CLAIM モジュールをパースする。
     *
     * @param reader
     *            CLAIM モジュールへの Reader
     */
    public void parse(BufferedReader reader) {

        try {
            SAXBuilder docBuilder = new SAXBuilder();
            Document doc = docBuilder.build(reader);
            Element root = doc.getRootElement();
            parseBody(root.getChild("MmlBody"));
            reader.close();

        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * CLAIM モジュールをパースして得た 患者来院情報 オブジェクトを返す。
     *
     * @return パース結果の 患者来院情報
     * @throws SQLException
     */
    public PatientVisitModel getProduct() throws SQLException {

        // PatientVisitModelを生成する
        PatientVisitModel model = new PatientVisitModel();

        // 患者モデルを設定する
        if (patientModel != null) {

            // Dery Distribution
            if (DERBY) {
                String pid = "D_" + patientModel.getPatientId();
                patientModel.setPatientId(pid);
            }

            model.setPatient(patientModel);

            // ORCA CLAIM 特有の処理を行う
            // 全角のスペースを半角スペースに変換する
            String fullName = patientModel.getFullName();
            fullName = fullName.replace("　", " ");
            patientModel.setFullName(fullName);
            int index = fullName.indexOf(" ");

            // FamilyName と GivenName を設定する
            if (patientModel.getFamilyName() == null && index > 0) {
                patientModel.setFamilyName(fullName.substring(0, index));
            }
            if (patientModel.getGivenName() == null && index > 0) {
                patientModel.setGivenName(fullName.substring(index + 1));
            }

            String kana = patientModel.getKanaName();
            if (kana != null) {
                kana = kana.replace("　", " ");
                patientModel.setKanaName(kana);
                int index2 = kana.indexOf(" ");
                if (patientModel.getKanaFamilyName() == null && index2 > 0) {
                    patientModel.setKanaFamilyName(kana.substring(0, index2));
                }
                if (patientModel.getKanaGivenName() == null && index2 > 0) {
                    patientModel.setKanaGivenName(kana.substring(index2 + 1));
                }
            }

            // 住所をEmbedded に変換する
            Collection<AddressModel> addresses = patientModel.getAddresses();
            if (addresses != null && addresses.size() > 0) {
                for (AddressModel bean : addresses) {
                    String addr = bean.getAddress();


                    char[] addrc = addr.toCharArray();
                    StringBuffer addrn = new StringBuffer();

                    for (int i = 0; i < addr.length(); i++) {
                        if (addrc[i] == '−') {
                            addrn.append('ー');
                        } else {
                            addrn.append(addrc[i]);
                        }
                    }
                    addr = addrn.toString();

                    SimpleAddressModel simple = new SimpleAddressModel();
                    simple.setZipCode(bean.getZipCode());
                    simple.setAddress(addr);
                    patientModel.setAddress(simple);
                    break;  // TODO
                }
            }

            // 電話をフィールドにする
            Collection<TelephoneModel> telephones = patientModel.getTelephones();
            if (telephones != null) {
                for (TelephoneModel bean : telephones) {
                    // MEMO へ設定
                    patientModel.setTelephone(bean.getMemo());
                }
            }

            // 健康保険モジュールを設定する
            if (pvtInsurnaces != null && pvtInsurnaces.size() > 0) {
                for (PVTHealthInsuranceModel bean : pvtInsurnaces) {
                    // 健康保険モジュールの BeanXml byte を生成し、
                    // 永続化のためのフォルダ HealthInsuranceModelに変換し
                    // それを患者属性に追加する
                    HealthInsuranceModel insModel = new HealthInsuranceModel();
                    insModel.setBeanBytes(BeanUtils.xmlEncode(bean));
                    // EJB 3.0 の関連を設定する
                    patientModel.addHealthInsurance(insModel);
                    insModel.setPatient(patientModel);
                }
            }
        }

        //
        // 受付情報を設定する
        // status=info ありだって、ヤレヤレ...
        //
        //if (pvtClaim != null && (!pvtClaim.getClaimStatus().equals("info"))) {
        if (pvtClaim != null) {

            // 1.3 リリースまでの暫定
            StringBuilder sb = new StringBuilder();
            sb.append(pvtClaim.getClaimDeptName());
            sb.append(",");
            sb.append(pvtClaim.getClaimDeptCode());
            sb.append(",");
            sb.append(pvtClaim.getAssignedDoctorName());
            sb.append(",");
            sb.append(pvtClaim.getAssignedDoctorId());
            sb.append(",");
            sb.append(pvtClaim.getJmariCode());
            sb.append(",");
            sb.append(pvtClaim.getClaimAppMemo());

            model.setDepartment(sb.toString());
            model.setPvtDate(pvtClaim.getClaimRegistTime());
            model.setInsuranceUid(pvtClaim.getInsuranceUid()); // UUID
        }

        return model;
    }

    /**
     * MmlBody 要素をパースする。
     *
     * @param body
     */
    public void parseBody(Element body) {

        // MmlModuleItem のリストを得る
        List children = body.getChildren("MmlModuleItem");

        // それをイテレートする
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {

            Element moduleItem = (Element) iterator.next();

            Element docInfo = moduleItem.getChild("docInfo");
            Element content = moduleItem.getChild("content");

            // docInfo の contentModuleType を調べる
            String attr = docInfo.getAttributeValue("contentModuleType");

            // contentModuleTypeで分岐する
            if (attr.equals("patientInfo")) {
                // 患者モジュールをパースする
                patientModel = new PatientModel();
                parsePatientInfo(docInfo, content);

            } else if (attr.equals("healthInsurance")) {

                // 健康保険モジュールをパースする
                String uuid = docInfo.getChild("docId").getChildTextTrim("uid");
                if (pvtInsurnaces == null) {
                    pvtInsurnaces = new ArrayList<PVTHealthInsuranceModel>();
                }
                curInsurance = new PVTHealthInsuranceModel();
                curInsurance.setGUID(uuid);
                pvtInsurnaces.add(curInsurance);
                parseHealthInsurance(docInfo, content);

            } else if (attr.equals("claim")) {
                // 受付情報をパースする
                pvtClaim = new PVTClaim();
                parseClaim(docInfo, content);
            } //else {

           // }
        }
    }

    /**
     * 患者モジュールをパースする。
     *
     * @param content
     *            患者要素
     */
    private void parsePatientInfo(Element docInfo, Element content) {

        List children = content.getChildren();

        //
        // 患者モジュールの要素をイテレートする
        //
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {

            Element child = (Element) iterator.next();
            String ename = child.getName();// MEMO;Unused?
            String qname = child.getQualifiedName();

            if (qname.equals("mmlCm:Id")) {
                String pid = child.getTextTrim();
                patientModel.setPatientId(pid);

            } else if (qname.equals("mmlNm:Name")) {
                List attrs = child.getAttributes();
                for (Iterator iter = attrs.iterator(); iter.hasNext();) {
                    Attribute attr = (Attribute) iter.next();
                    if (attr.getName().equals("repCode")) {
                        curRepCode = attr.getValue();
                    } /*else if (attr.getName().equals("tableId")) {
                    }*/
                }
            } else if (qname.equals("mmlNm:family")) {
                if (curRepCode.equals("P")) {
                    patientModel.setKanaFamilyName(child.getTextTrim());
                } else if (curRepCode.equals("I")) {
                    patientModel.setFamilyName(child.getTextTrim());
                } else if (curRepCode.equals("A")) {
                    patientModel.setRomanFamilyName(child.getTextTrim());
                }
            } else if (qname.equals("mmlNm:given")) {
                if (curRepCode.equals("P")) {
                    patientModel.setKanaGivenName(child.getTextTrim());
                } else if (curRepCode.equals("I")) {
                    patientModel.setGivenName(child.getTextTrim());
                } else if (curRepCode.equals("A")) {
                    patientModel.setRomanGivenName(child.getTextTrim());
                }
            } else if (qname.equals("mmlNm:fullname")) {
                if (curRepCode.equals("P")) {
                    patientModel.setKanaName(child.getTextTrim());
                } else if (curRepCode.equals("I")) {
                    patientModel.setFullName(child.getTextTrim());
                } else if (curRepCode.equals("A")) {
                    patientModel.setRomanName(child.getTextTrim());
                }
            } else if (qname.equals("mmlPi:birthday")) {
                patientModel.setBirthday(child.getTextTrim());
            } else if (qname.equals("mmlPi:sex")) {
                patientModel.setGender(child.getTextTrim());
            } else if (qname.equals("mmlAd:Address")) {
                curAddress = new AddressModel();
                patientModel.addAddress(curAddress);
                List attrs = child.getAttributes();
                for (Iterator iter = attrs.iterator(); iter.hasNext();) {
                    Attribute attr = (Attribute) iter.next();
                    if (attr.getName().equals("addressClass")) {
                        curRepCode = attr.getValue();
                        curAddress.setAddressType(attr.getValue());
                    } else if (attr.getName().equals("tableId")) {
                        curAddress.setAddressTypeCodeSys(attr.getValue());
                    }
                }
            } else if (qname.equals("mmlAd:full")) {
                curAddress.setAddress(child.getTextTrim());
            } else if (qname.equals("mmlAd:zip")) {
                curAddress.setZipCode(child.getTextTrim());
            } else if (qname.equals("mmlPh:Phone")) {
                curTelephone = new TelephoneModel();
                patientModel.addTelephone(curTelephone);
            } else if (qname.equals("mmlPh:area")) {
                String val = child.getTextTrim();
                // ORCA
                if (val != null && val.startsWith("?") == false) {
                    curTelephone.setArea(child.getTextTrim());
                }
            } else if (qname.equals("mmlPh:city")) {
                String val = child.getTextTrim();
                // ORCA
                if (val != null && val.startsWith("?") == false) {
                    curTelephone.setCity(val);
                }
            } else if (qname.equals("mmlPh:number")) {
                String val = child.getTextTrim();
                // ORCA
                if (val != null && val.startsWith("?") == false) {
                    curTelephone.setNumber(val);
                }
            } else if (qname.equals("mmlPh:memo")) {
                // ORCA
                curTelephone.setMemo(child.getTextTrim());
            }
            parsePatientInfo(docInfo, child);
        }
    }

    /**
     * 健康保険モジュールをパースする。
     *
     * @param content
     *            健康保険要素
     */
    private void parseHealthInsurance(Element docInfo, Element content) {

        // HealthInsuranceModule を得る
        Element hModule = content.getChild("HealthInsuranceModule", mmlHi);
        if (hModule == null) {
            return;
        }

        // InsuranceClass を解析する
        Element insuranceClass = hModule.getChild("insuranceClass", mmlHi);
        if (insuranceClass != null) {
            curInsurance.setInsuranceClass(insuranceClass.getTextTrim());
            if (insuranceClass.getAttribute("ClassCode", mmlHi) != null) {
                curInsurance.setInsuranceClassCode(insuranceClass.getAttributeValue("ClassCode", mmlHi));
            }
            if (insuranceClass.getAttribute("tableId", mmlHi) != null) {
                curInsurance.setInsuranceClassCodeSys(insuranceClass.getAttributeValue("tableId", mmlHi));
            }
        }

        // insurance Number を得る
        if (hModule.getChildTextTrim("insuranceNumber", mmlHi) != null) {
            curInsurance.setInsuranceNumber(hModule.getChildTextTrim("insuranceNumber", mmlHi));
        }

        // clientId を得る
        Element clientId = hModule.getChild("clientId", mmlHi);
        if (clientId != null) {
            if (clientId.getChild("group", mmlHi) != null) {
                curInsurance.setClientGroup(clientId.getChildTextTrim("group", mmlHi));
            }
            if (clientId.getChild("number", mmlHi) != null) {
                curInsurance.setClientNumber(clientId.getChildTextTrim("number", mmlHi));
            }
        }

        // familyClass を得る
        if (hModule.getChild("familyClass", mmlHi) != null) {
            curInsurance.setFamilyClass(hModule.getChildTextTrim("familyClass", mmlHi));
        }

        // startDateを得る
        if (hModule.getChild("startDate", mmlHi) != null) {
            curInsurance.setStartDate(hModule.getChildTextTrim("startDate", mmlHi));
        }

        // expiredDateを得る
        if (hModule.getChild("expiredDate", mmlHi) != null) {
            curInsurance.setExpiredDate(hModule.getChildTextTrim("expiredDate", mmlHi));
        }

        // payInRatio を得る
        if (hModule.getChild("paymentInRatio", mmlHi) != null) {
            curInsurance.setPayInRatio(hModule.getChildTextTrim("paymentInRatio", mmlHi));
        }

        // payOutRatio を得る
        if (hModule.getChild("paymentOutRatio", mmlHi) != null) {
            curInsurance.setPayOutRatio(hModule.getChildTextTrim("paymentOutRatio", mmlHi));
        }

        //
        // publicInsurance をパースする
        //
        Element publicInsurance = hModule.getChild("publicInsurance", mmlHi);
        if (publicInsurance != null) {
            List children = publicInsurance.getChildren();
            for (Iterator iterator = children.iterator(); iterator.hasNext();) {

                // publicInsuranceItem を得る
                Element publicInsuranceItem = (Element) iterator.next();

                curPublicItem = new PVTPublicInsuranceItemModel();
                curInsurance.addPvtPublicInsuranceItem(curPublicItem);

                // priority
                if (publicInsuranceItem.getAttribute("priority", mmlHi) != null) {
                    curPublicItem.setPriority(publicInsuranceItem.getAttributeValue("priority", mmlHi));
                }

                // providerName
                if (publicInsuranceItem.getChild("providerName", mmlHi) != null) {
                    curPublicItem.setProviderName(publicInsuranceItem.getChildTextTrim("providerName", mmlHi));
                }

                // provider
                if (publicInsuranceItem.getChild("provider", mmlHi) != null) {
                    curPublicItem.setProvider(publicInsuranceItem.getChildTextTrim("provider", mmlHi));
                }

                // recipient
                if (publicInsuranceItem.getChild("recipient", mmlHi) != null) {
                    curPublicItem.setRecipient(publicInsuranceItem.getChildTextTrim("recipient", mmlHi));
                }

                // startDate
                if (publicInsuranceItem.getChild("startDate", mmlHi) != null) {
                    curPublicItem.setStartDate(publicInsuranceItem.getChildTextTrim("startDate", mmlHi));
                }

                // expiredDate
                if (publicInsuranceItem.getChild("expiredDate", mmlHi) != null) {
                    curPublicItem.setExpiredDate(publicInsuranceItem.getChildTextTrim("expiredDate", mmlHi));
                }

                // paymentRatio
                Element paymentRatio = publicInsuranceItem.getChild("paymentRatio", mmlHi);
                if (paymentRatio != null) {
                    curPublicItem.setPaymentRatio(paymentRatio.getTextTrim());
                    if (paymentRatio.getAttribute("ratioType", mmlHi) != null) {
                        curPublicItem.setPaymentRatioType(paymentRatio.getAttributeValue("ratioType", mmlHi));
                    }
                }
            }
        }
    }

    /**
     * 受付情報をパースする。
     *
     * @param content
     *            受付情報要素
     */
    private void parseClaim(Element docInfo, Element content) {

        //
        // ClaimModule の DocInfo に含まれる診療科と担当医を抽出する
        //
        Element creatorInfo = docInfo.getChild("CreatorInfo", mmlCi);
        Element psiInfo = creatorInfo.getChild("PersonalizedInfo", mmlPsi);

        // 担当医ID
        pvtClaim.setAssignedDoctorId(psiInfo.getChildTextTrim("Id", mmlCm));

        // 担当医名
        Element personName = psiInfo.getChild("personName", mmlPsi);
        Element name = personName.getChild("Name", mmlNm);
        if (name != null) {
            Element fullName = name.getChild("fullname", mmlNm);
            if (fullName != null) {
                pvtClaim.setAssignedDoctorName(fullName.getTextTrim());
            }
        }

        // 施設情報 JMARI 4.0 から
        Element facility = psiInfo.getChild("Facility", mmlFc);
        pvtClaim.setJmariCode(facility.getChildTextTrim("Id", mmlCm));

        // 診療科情報
        Element dept = psiInfo.getChild("Department", mmlDp);
        pvtClaim.setClaimDeptName(dept.getChildTextTrim("name", mmlDp));
        pvtClaim.setClaimDeptCode(dept.getChildTextTrim("Id", mmlCm));

        // ClaimInfoを解析する
        Element claimModule = content.getChild("ClaimModule", claim);
        Element claimInfo = claimModule.getChild("information", claim);

        // status
        pvtClaim.setClaimStatus(claimInfo.getAttributeValue("status", claim));

        // registTime
        pvtClaim.setClaimRegistTime(claimInfo.getAttributeValue("registTime", claim));

        // admitFlag
        pvtClaim.setClaimAdmitFlag(claimInfo.getAttributeValue("admitFlag", claim));

        // insuranceUid
        pvtClaim.setInsuranceUid(claimInfo.getAttributeValue("insuranceUid", claim));

        //Category
        String memo = claimInfo.getChild("appoint", claim).getChild("memo", claim).getText().trim();
        pvtClaim.setClaimAppMemo(memo);
    }
}
