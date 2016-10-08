#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do

    # データベースが更新されれば動かない
    select('TabbedPane', '患者検索')

    click('AddressTipsTable', '{0, ID}')
    click('JTableHeader1', 'ID')
    click('AddressTipsTable', '{0, ID}')
    rightclick('AddressTipsTable', '{0, ID}')
    assert_p('AddressTipsTable', 'Text', '00001', '{0, ID}')
    assert_p('AddressTipsTable', 'Text', '00002', '{1, ID}')
    assert_p('AddressTipsTable', 'Text', '00003', '{2, ID}')
    assert_p('AddressTipsTable', 'Text', '00004', '{3, ID}')
    assert_p('AddressTipsTable', 'Text', '00005', '{4, ID}')
    assert_p('AddressTipsTable', 'Text', '00006', '{5, ID}')
    assert_p('AddressTipsTable', 'Text', '00007', '{6, ID}')
    assert_p('AddressTipsTable', 'Text', '00008', '{7, ID}')
    assert_p('AddressTipsTable', 'Text', '00009', '{8, ID}')
    assert_p('AddressTipsTable', 'Text', '00010', '{9, ID}')
    assert_p('AddressTipsTable', 'Text', '00011', '{10, ID}')
    assert_p('AddressTipsTable', 'Text', '00012', '{11, ID}')
    assert_p('AddressTipsTable', 'Text', '00013', '{12, ID}')
    assert_p('AddressTipsTable', 'Text', '00014', '{13, ID}')
    assert_p('AddressTipsTable', 'Text', '00015', '{14, ID}')
    assert_p('AddressTipsTable', 'Text', '00016', '{15, ID}')
    assert_p('AddressTipsTable', 'Text', '00020', '{16, ID}')
    assert_p('AddressTipsTable', 'Text', '00022', '{17, ID}')
    assert_p('AddressTipsTable', 'Text', '00025', '{18, ID}')
    assert_p('AddressTipsTable', 'Text', '00032', '{19, ID}')
    assert_p('AddressTipsTable', 'Text', '00036', '{21, ID}')
    assert_p('AddressTipsTable', 'Text', '00037', '{22, ID}')
    assert_p('AddressTipsTable', 'Text', '00038', '{23, ID}')
    select('TextField', '喘息')

    click('メモ')
    click('AddressTipsTable', '{0, ID}')
    assert_p('AddressTipsTable', 'Text', '00025', '{0, ID}')
    assert_p('AddressTipsTable', 'Text', '00037', '{1, ID}')
    assert_p('AddressTipsTable', 'Text', '00038', '{2, ID}')
    select('TextField', '')
    click('アレルギー')
    click('AddressTipsTable', '{0, ID}')
    assert_p('AddressTipsTable', 'Text', '00004', '{0, ID}')
    assert_p('AddressTipsTable', 'Text', '山田 花子', '{1, 氏名}')
    assert_p('AddressTipsTable', 'Text', '武田 有樹', '{2, 氏名}')
    select('TabbedPane', '患者検索')
    rightclick('TextField')
    click('Table', '{3, 月}')
    assert_p('TextField', 'Text', '2010-05-17')
    assert_p('AddressTipsTable', 'Text', '00004', '{0, ID}')
    assert_p('AddressTipsTable', 'Text', '00005', '{1, ID}')
    assert_p('AddressTipsTable', 'Text', '00006', '{2, ID}')
    assert_p('AddressTipsTable', 'Text', '00006', '{3, ID}')
    assert_p('AddressTipsTable', 'Text', '00007', '{4, ID}')
    assert_p('AddressTipsTable', 'Text', '00009', '{5, ID}')
    assert_p('AddressTipsTable', 'Text', '00012', '{6, ID}')
    assert_p('AddressTipsTable', 'Text', '00014', '{7, ID}')
    select('TabbedPane', '受付リスト')

  end
end
