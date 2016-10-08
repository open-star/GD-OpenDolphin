
require 'common/on_login_window'

def on_setting_window
  
  on_login_window { click('設 定') }

  with_window('環境設定-OpenDolphin') do
    yield
  end
end

def on_setting_window_for(name)
  on_setting_window do
    select(name, 'true')
    yield
  end
end
