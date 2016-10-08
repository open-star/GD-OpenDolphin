
def on_login_window(version = nil)
  with_window("ログイン-OpenDolphin-#{version || DOLPHIN_VERSION}") do
    yield
  end
end

