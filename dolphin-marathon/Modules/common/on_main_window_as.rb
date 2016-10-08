require 'common/login_as'

def on_main_window_as(name, pass)
  name = login_as(name, pass)
  with_window("メインウインドウ-OpenDolphin | #{name} | 医師") do
    yield name
  end
end

