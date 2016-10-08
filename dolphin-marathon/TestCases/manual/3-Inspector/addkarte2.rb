#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'manual/on_inspector_for'

def test
  java_recorded_version = '1.6.0_20'

  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do |_, _, name|

      click('new_24')

      with_window('新規カルテ-OpenDolphin') do
        select('List', '[01270016  協会けんぽ]')
        select('List', '[01270016  協会けんぽ]')
        select('全てコピー', 'true')
        click('Box$Filler5', 188, 199)
        select('前回処方を適用', 'true')
        select('全てコピー', 'true')
        assert_p('前回処方を適用', 'Text', 'false')
        assert_p('前回処方を適用', 'Text', 'false')
        assert_p('全てコピー', 'Text', 'true')
        assert_p('空白の新規カルテ', 'Text', 'false')
        assert_p('別ウィンドウで編集', 'Text', 'true')
        assert_p('タブパネルへ追加', 'Text', 'false')
        assert_p('入院', 'Text', 'false')
        # TODO: 作った時刻が入力されるためにちょっとずれる
        #       しかも、「今の時刻」をクリックしても開いた時間になってしまう
        # click('今の時刻')
        # assert_p('Label4', 'Text', Time.now.strftime("%Y-%m-%d %H:%M:%S").to_s)

        select('空白の新規カルテ', 'true')

        click($fixture.variables.ok_label)
      end

      with_window("ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | #{name} | 医師") do
        select('TextPane1', "\n \n\n \n\n発熱　37.5℃\n悪寒あり\n\n本日は入浴禁止。\nシャワーのみ。\n\n\n\n")
        select_menu('挿 入>>処 方>>喘息13kg')
        click('save_24')
      end

      with_window('ドキュメント保存-OpenDolphin') { click('保存して送信') }
    end
  end
end

