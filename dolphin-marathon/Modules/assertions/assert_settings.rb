
require 'test/unit/assertions'
include Test::Unit::Assertions

def assert_settings(name, expected, arg = nil)
  actual = get_setting(name, arg)
  assert_equal(expected, actual)
end


def get_setting(name, arg = nil)
  case name
  when 'Preferences'
    $fixture.preferences.getPreferences
  when 'DolphinPrincipal'
    $fixture.preferences.getDolphinPrincipal
  when 'ProviderURL'
    $fixture.preferences.getProviderURL
  when 'Name'
    $fixture.preferences.getName
  when 'UserModel'
    $fixture.preferences.getUserModel
  when 'UserId'
    $fixture.preferences.getUserId
  when 'FacilityId'
    $fixture.preferences.getFacilityId
  when 'OrcaVersion'
    $fixture.preferences.getOrcaVersion
  when 'JMARICode'
    $fixture.preferences.getJMARICode
  when 'UserType'
    $fixture.preferences.getUserType
  when 'HostAddress'
    $fixture.preferences.getHostAddress
  when 'DbAddress'
    $fixture.preferences.getDbAddress
  when 'HostPort'
    $fixture.preferences.getHostPort
  when 'TopInspector'
    $fixture.preferences.getTopInspector
  when 'SecondInspector'
    $fixture.preferences.getSecondInspector
  when 'ThirdInspector'
    $fixture.preferences.getThirdInspector
  when 'ForthInspector'
    $fixture.preferences.getForthInspector
  when 'LocateByPlatform'
    $fixture.preferences.getLocateByPlatform
  when 'PDFStore'
    $fixture.preferences.getPDFStore
  when 'FetchKarteCount'
    $fixture.preferences.getFetchKarteCount
  when 'ScrollKarteV'
    $fixture.preferences.getScrollKarteV
  when 'AscendingKarte'
    $fixture.preferences.getAscendingKarte
  when 'KarteExtractionPeriod'
    $fixture.preferences.getKarteExtractionPeriod
  when 'ShowModifiedKarte'
    $fixture.preferences.getShowModifiedKarte
  when 'ShowUnsendKarte'
    $fixture.preferences.getShowUnsendKarte
  when 'ShowSendKarte'
    $fixture.preferences.getShowSendKarte
  when 'AscendingDiagnosis'
    $fixture.preferences.getAscendingDiagnosis
  when 'DiagnosisExtractionPeriod'
    $fixture.preferences.getDiagnosisExtractionPeriod
  when 'DefaultZyozaiNum'
    $fixture.preferences.getDefaultZyozaiNum
  when 'DefaultMizuyakuNum'
    $fixture.preferences.getDefaultMizuyakuNum
  when 'DefaultSanyakuNum'
    $fixture.preferences.getDefaultSanyakuNum
  when 'DefaultRpNum'
    $fixture.preferences.getDefaultRpNum
  when 'LaboTestExtractionPeriod'
    $fixture.preferences.getLaboTestExtractionPeriod
  when 'ConfirmAtNew'
    $fixture.preferences.getConfirmAtNew
  when 'CreateKarteMode'
    $fixture.preferences.getCreateKarteMode
  when 'PlaceKarteMode'
    $fixture.preferences.getPlaceKarteMode
  when 'ConfirmAtSave'
    $fixture.preferences.getConfirmAtSave
  when 'PrintKarteCount'
    $fixture.preferences.getPrintKarteCount
  when 'SaveKarteMode'
    $fixture.preferences.getSaveKarteMode
  when 'SendClaim'
    $fixture.preferences.getSendClaim
  when 'SendClaimSave'
    $fixture.preferences.getSendClaimSave
  when 'SendClaimTmp'
    $fixture.preferences.getSendClaimTmp
  when 'SendClaimModify'
    $fixture.preferences.getSendClaimModify
  when 'DefaultKarteTitle'
    $fixture.preferences.getDefaultKarteTitle
  when 'SendDiagnosis'
    $fixture.preferences.getSendDiagnosis
  when 'ClaimHostName'
    $fixture.preferences.getClaimHostName
  when 'ClaimEncoding'
    $fixture.preferences.getClaimEncoding
  when 'ClaimAddress'
    $fixture.preferences.getClaimAddress
  when 'ClaimPort'
    $fixture.preferences.getClaimPort
  when 'UseAsPVTServer'
    $fixture.preferences.getUseAsPVTServer
  when 'IsHospital'
    $fixture.preferences.getIsHospital
  when 'JoinAreaNetwork'
    $fixture.preferences.getJoinAreaNetwork
  when 'AreaNetworkName'
    $fixture.preferences.getAreaNetworkName
  when 'AreaNetworkFacilityId'
    $fixture.preferences.getAreaNetworkFacilityId
  when 'AreaNetworkCreatorId'
    $fixture.preferences.getAreaNetworkCreatorId
  when 'SendMML'
    $fixture.preferences.getSendMML
  when 'MMLVersion'
    $fixture.preferences.getMMLVersion
  when 'MMLEncoding'
    $fixture.preferences.getMMLEncoding
  when 'MIMEEncoding'
    $fixture.preferences.getMIMEEncoding
  when 'UploaderIPAddress'
    $fixture.preferences.getUploaderIPAddress
  when 'UploadShareDirectory'
    $fixture.preferences.getUploadShareDirectory
  when 'UseProxy'
    $fixture.preferences.getUseProxy
  when 'ProxyHost'
    $fixture.preferences.getProxyHost
  when 'ProxyPort'
    $fixture.preferences.getProxyPort
  when 'LastModify'
    $fixture.preferences.getLastModify
  when 'Directions'
    $fixture.preferences.getDirections
  when 'Direction'
    $fixture.preferences.getDirection(arg.to_i)
  when 'CcDirections'
    $fixture.preferences.getCcDirections
  when 'Valid'
    $fixture.preferences.isValid
  when 'AutoOutcomeInput'
    $fixture.preferences.isAutoOutcomeInput
  when 'ReplaceStamp'
    $fixture.preferences.isReplaceStamp
  when 'StampSpace'
    $fixture.preferences.isStampSpace
  when 'LaboFold'
    $fixture.preferences.isLaboFold
  when 'UseTop15AsTitle'
    $fixture.preferences.isUseTop15AsTitle
  when 'Claim01'
    $fixture.preferences.isClaim01
  when 'OffsetOutcomeDate'
    pref = get_setting('Preferences')
    pref.getInt('diagnosis.offsetOutcomeDate', -7)
  else
    raise "No such setting: #{name}"
  end
end
