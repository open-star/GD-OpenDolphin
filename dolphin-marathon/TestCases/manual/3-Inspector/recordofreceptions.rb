#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'manual/on_inspector_for'

#治療履歴を照会する
def test
  java_recorded_version = '1.6.0_20'


  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    on_inspector_for('00038', name) do |_, _, name|

      #治療履歴の処方、処置、、、、を選択し、表示される内容が正しいか確認する
      select('tabbedPane', '治療履歴')
      select('orderCombo', '処置')
      click('forwd_16')
      select('orderCombo', '指導')
      click('forwd_16')
      click('back_16')
      select('orderCombo', 'ラボテスト')
      click('forwd_16')
      click('back_16')
      select('orderCombo', '生体検査')
      click('forwd_16')
      click('back_16')
      select('orderCombo', '画像診断')
      click('forwd_16')
      click('back_16')
      click('CareMapDocumentPanel', 166, 230)
      select('orderCombo', '処方')
      assert_p('table3', 'Text', '2010-05-24', '{0, 実施日}')
      assert_p('table3', 'Text', '2010-05-24', '{1, 実施日}')
      select('orderCombo', '処置')
      assert_p('table3', 'Text', '2010-05-25', '{0, 実施日}')
      select('orderCombo', '指導')
      assert_p('table3', 'Text', '', '{0, 実施日}')
      assert_p('table3', 'Text', '', '{0, 実施日}')
      select('orderCombo', 'ラボテスト')
      assert_p('table3', 'Text', '2010-05-26', '{0, 実施日}')
      select('orderCombo', '生体検査')
      assert_p('table3', 'Text', '', '{0, 実施日}')
      assert_p('table3', 'Text', '', '{0, 実施日}')
      select('orderCombo', '画像診断')
      assert_p('table3', 'Text', '2010-05-24', '{0, 実施日}')
      select('orderCombo', '処方')
      select('orderCombo', '画像診断')
      # assert_p('contents', 'Text', "<HTML><BODY><TT><FONT SIZE=\"${hints.getFontSize()}\" COLOR=\"${hints.getBackgroundAs16String()}\">\n<TABLE BORDER=\"${hints.getBorder()}\" CELLSPACING=\"${hints.getCellSpacing()}\" CELLPADDING=\"${hints.getCellPadding()}\">\n<TR BGCOLOR=\"${hints.getLabelColorAs16String()}\">\n<TD COLSPAN=\"3\">画像診断(胸部レントゲン)</TD>\n</TR>\n<TR>\n<TD>・胸部レントゲン</TD>\n<TD> X 1.0</TD>\n<TD></TD>\n</TR>\n<TR>\n<TD>・胸部レントゲン</TD>\n<TD> X 1.0</TD>\n<TD></TD>\n</TR>\n<TR>\n<TD>・単純撮影（撮影）</TD>\n<TD> X 1.0</TD>\n<TD></TD>\n</TR>\n<TR>\n<TD>・単純撮影（撮影）</TD>\n<TD> X 1.0</TD>\n<TD></TD>\n</TR>\n<TR>\n<TD>・単純撮影（イ）の写真診断</TD>\n<TD> X 1.0</TD>\n<TD></TD>\n</TR>\n<TR>\n<TD>・単純撮影（イ）の写真診断</TD>\n<TD> X 1.0</TD>\n<TD></TD>\n</TR>\n<TR>\n<TD>・大角</TD>\n<TD> X 1.0</TD>\n<TD>枚</TD>\n</TR>\n<TR>\n<TD>・大角</TD>\n<TD> X 1.0</TD>\n<TD>枚</TD>\n</TR>\n<TR>\n<TD COLSPAN=\"3\"></TD>\n</TR>\n</TABLE>\n</BODY></HTML>")
    end
  end
end

