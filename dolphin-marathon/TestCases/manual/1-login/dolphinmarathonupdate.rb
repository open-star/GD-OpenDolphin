#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'

def test
  java_recorded_version = '1.6.0_20'


  on_main_window_as('MarathonDolphin', 'dolphin') do

    select_menu('ツール>>プロフィール変更...')

    with_window('プロフィール変更-OpenDolphin') do
      select('PasswordField', 'aaaaaa')
      select('PasswordField1', 'aaaaaa')
      click('変更')
      # Mac: OK, Gnome: '了解'
      with_window('パスワード変更-OpenDolphin') { click($fixture.variables.ok_label) }
    end

    select_menu('ツール>>プロフィール変更...')

    with_window('プロフィール変更-OpenDolphin') do
      select('PasswordField', 'dolphin')
      select('PasswordField1', 'dolphin')
      click('変更')                       
      # Mac: OK, Gnome: '了解'
      with_window('パスワード変更-OpenDolphin') { click($fixture.variables.ok_label) }
    end
  end
end
