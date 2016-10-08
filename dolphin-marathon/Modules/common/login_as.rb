
require 'common/on_login_window'

def login_as(name, password)
  on_login_window do
    select('TextField', name)
    select('PasswordField', password)
    click('ログイン')
  end

  name
end

