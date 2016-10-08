include_class 'open.dolphin.client.Dolphin'
include_class 'open.dolphin.project.GlobalVariables'
include_class 'java.util.prefs.Preferences'

require 'rbconfig'

DOLPHIN_VERSION = '1.8'
COPYRIGHT_DOLPHIN = '2010 Dolphin Project.'
COPYRIGHT_GOODDAY = '2001-2010 Good-Day Inc.'

module SettingManager
  class << self
    attr_accessor :preferences_target
   end
end

class Fixture
  
  class Variables
    attr_accessor :ok_label
  end
  
  attr_reader :preferences
  attr_reader :variables

  def start_application
    args = []
    Dolphin.main(args.to_java:String)
  end

  def start_environment
    GlobalVariables.createGlobalVariables()
    @preferences = GlobalVariables.getGlobalVariables()
    
    @variables = Variables.new
    case Config::CONFIG['host_os']
    when /darwin/
      variables.ok_label = 'OK'
    when /linux/
      variables.ok_label = '了解'
    end
  end

  def teardown
  end

  def setup
    start_application
    start_environment
  end

  def test_setup
    SettingManager.preferences_target = nil
  end

end

$fixture = Fixture.new

