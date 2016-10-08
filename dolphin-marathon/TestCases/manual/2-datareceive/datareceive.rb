#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do

    select('TabbedPane', '受付リスト')

    click('kutu01')

    # Memo: 他の受付があればずれる
    assert_p('RowTipsTable', 'Text', 'ドルフィン マラソン', '{0, 氏   名}')
    doubleclick('RowTipsTable', '{0, 氏   名}')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | MarathonDolphin | 医師') do
      # MEMO: 更新されていれば落ちる
      assert_p('CompositeArea', 'Text', '幼少期に小児喘息')
    end
  end
end

