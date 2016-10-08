#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'common/wait_table'
require 'manual/on_inspector_for'

def test
  #スタンプメーカーでスタンプを作成し、そのスタンプをカルテに貼るテスト
  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    with_window('スタンプ箱-OpenDolphin') {
      select('UserStampBox', '処 置')
      assert_p('UserStampBox', 'Text', '処 置')
      select('tools_24', 'true')
      select('tabbedPane', '内用・外用薬')
      assert_p('tabbedPane', 'Text', '内用・外用薬')
      select('TextField1', 'セフス')
      select('部分一致1', 'true')
      wait_table('table3', '{0, 名  称}')
      doubleclick('table3', '{0, 名  称}')
      wait_table('setTable10', '{0, コード}')
      assert_p('setTable10', 'Text', 'セフスパン細粒５０ｍｇ', '{0, 診療内容}')
      select('部分一致1', 'false')
      select('TextField1', 'ノブフェ')
      keystroke('TextField1', 'Enter')
      wait_table_pattern('table3', '{0, 名  称}', /ノブフェン/)
      doubleclick('table3', '{0, 名  称}')
      wait_table('setTable10', '{1, コード}')
      assert_p('setTable10', 'Text', 'ノブフェン錠６０ｍｇ', '{1, 診療内容}')

      select('stampNameField11', 'セフスパンノブフェン酸')
      click('forwd_16')
      assert_p('StampTree5', 'Text', 'セフスパンノブフェン酸', 'セフスパンノブフェン酸')
      select('StampTree5', '[セフスパンノブフェン酸]')
      
      window_closed('スタンプ箱-OpenDolphin')
    }

    on_inspector_for('00038', name) do |_, _, name|

      click('new_24')

      with_window('新規カルテ-OpenDolphin') {
        select('List', '[01270016  協会けんぽ]')
        click($fixture.variables.ok_label)
      }
      
      with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | MarathonDolphin | 医師') {
        select('TextPane', "\n \nスタンプメーカーで作成したスタンプを\n貼付けました\n")
        select_menu('挿 入>>処 置>>セフスパンノブフェン酸')
        # assert_p('TextPane1', 'Text', "<HTML><BODY><TT><FONT SIZE=\"12\" COLOR=\"\#ffffff\">\n<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"3\">\n<TR BGCOLOR=\"\#FFCED9\">\n<TD COLSPAN=\"3\">処 置(セフスパンノブフェン酸) X 1</TD>\n</TR>\n<TR>\n<TD>・セフスパン細粒50mg</TD>\n<TD> X 1.0</TD>\n<TD>g</TD>\n</TR>\n<TR>\n<TD>・ノブフェン錠60mg</TD>\n<TD> X 3</TD>\n<TD>錠</TD>\n</TR>\n<TR>\n<TD COLSPAN=\"3\"></TD>\n</TR>\n</TABLE>\n</BODY></HTML>")
        window_closed("ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | #{name} | 医師")
      }

      with_window('未保存処理-OpenDolphin') {
        click('保存')
      }

      with_window('ドキュメント保存-OpenDolphin') {
        click('保存して送信')
      }
    end
  end
end

