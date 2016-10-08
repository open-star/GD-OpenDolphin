
require 'test/unit/assertions'
include Test::Unit::Assertions

def assert_preference(key, expected)
  actual = get_preference(key)
  assert_equal expected, actual 
end

def get_preference(key)
  if SettingManager.preferences_target.nil?
    raise 'Please set preferences_target class'
  end
  prefs = Preferences.userNodeForPackage(SettingManager.preferences_target)
  prefs.get(key, '')
end
