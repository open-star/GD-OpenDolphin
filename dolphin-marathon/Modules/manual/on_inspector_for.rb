
# on_main_window_as の中で使う

require 'test/unit/assertions'
include Test::Unit::Assertions

def on_inspector_for(target_id, login_name)
  select('TabbedPane', '受付リスト')

  count = get_p('RowTipsTable', 'RowCount').to_i
  count.times do |i|
    id = get_p('RowTipsTable', 'Text', "{#{i}, 患者ID}")
    if id == target_id
      patient_name = get_p('RowTipsTable', 'Text' "{#{i}, 氏   名}")
      click('RowTipsTable', "{#{i}, 氏   名}")
      rightclick('RowTipsTable', 2, "{#{i}, 氏   名}")
      # MEMO: これを入れると２重に開いてしまう
      # select_menu('カルテを開く')
      # FIXME: ドルフィンマラソンに限定してしまっている
      with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | MarathonDolphin | 医師') {
        yield id, patient_name, login_name
      }
      return
    end
  end
  flunk "No patient found. #{target_id}"
end

