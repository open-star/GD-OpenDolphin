#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'common/wait_table'
require 'manual/on_inspector_for'

#傷病名登録　エディタから発行
def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do |_, _, name|


      select('tabbedPane', '傷病名')
      select('tabbedPane', '参 照')
      select('tabbedPane', '傷病名')
      click('add_16')
      select_menu('エディタから発行')

      with_window('傷病名エディタ-OpenDolphin') {
        select('searchField', '急性')
        keystroke('searchField', 'Enter')
        wait_table('table1', '{0, コード}')
        assert_p('table1', 'Text', '急性', '{0, 名  称}')
        select('findField', '甲状腺炎')
        click('部位')
        click('性質')
        click('病名')
        click('接尾')
        click('病名')
        wait_table('table1', '{0, コード}')
        assert_p('table1', 'Text', '急性化膿性甲状腺炎', '{0, 名  称}')
        assert_p('table1', 'Text', '2450001', '{0, コード}')
        assert_p('table1', 'Text', '2450002', '{1, コード}')
        assert_p('table1', 'Text', '2451001', '{2, コード}')
        assert_p('table1', 'Text', '2452011', '{3, コード}')
        assert_p('table1', 'Text', '2458001', '{4, コード}')
        assert_p('table1', 'Text', '2459001', '{5, コード}')
        assert_p('table1', 'Text', '8832685', '{6, コード}')
        assert_p('table1', 'Text', '8835204', '{7, コード}')
        assert_p('table1', 'Text', '8838379', '{8, コード}')
        assert_p('table1', 'Text', '8839183', '{9, コード}')
        assert_p('table1', 'Text', '8839863', '{10, コード}')
        assert_p('table1', 'Text', '8840069', '{11, コード}')
        assert_p('table1', 'Text', '2452001', '{12, コード}')
        assert_p('table1', 'Text', '2458002', '{13, コード}')
        window_closed('傷病名エディタ-OpenDolphin')
      }

      with_window("ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | #{name} | 医師") {
        click('add_16')
        select_menu('エディタから発行')
      }

      with_window('傷病名エディタ-OpenDolphin') {
        select('searchField', '急性')
        select('findField', '甲状腺炎')
        click('病名')
        click('table1', '{1, 名  称}')
        doubleclick('table1', '{1, カ ナ}')
        rightclick('table1', '{1, カ ナ}')
        click('table1', 4, '{1, カ ナ}')
        doubleclick('table1', '{1, 名  称}')
        click('DiagnosisTablePanel', 117, 38)
        click('table', '{1, コード}')
        click('del_16')
        click('del_16')
        click('lgicn_16')
      }

      with_window("ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | #{name} | 医師") {
        click('Table', '{0, 疾患名/修飾語}')
        assert_p('Table', 'Text', '急性甲状腺炎', '{0, 疾患名/修飾語}')
        select('tabbedPane', '参 照')
        select('tabbedPane', '傷病名')
        select('tabbedPane', '参 照')
        window_closed("ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | #{name} | 医師")

        with_window('未保存処理-OpenDolphin') { click('保存') }
        
        # MEMO: 不明
        # with_window('OpenDolphin: CLAIM 送信') { click('OK') }
      }

      with_window("メインウインドウ-OpenDolphin | #{name} | 医師") do
        on_inspector_for('00038', name) do |_, _, name|
          select('tabbedPane', '傷病名')
          assert_p('Table1', 'Text', '急性甲状腺炎', '{0, 疾患名/修飾語}')
        end
      end
    end
  end
end
