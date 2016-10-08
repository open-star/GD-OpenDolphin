#{{{ Marathon Fixture
require 'default'
#}}} Marathon Fixture

require 'common/on_main_window_as'
require 'common/wait_table'
require 'manual/on_inspector_for'

def test
#傷病名スタンプを作成し、カルテの傷病名に登録する  
  on_main_window_as('MarathonDolphin', 'dolphin') do |name|

    with_window('スタンプ箱-OpenDolphin') {
      select('UserStampBox', '傷病名')
      assert_p('UserStampBox', 'Text', '傷病名')
      select('tools_24', 'true')
      select('searchField', '皮膚')
      sleep 0.1 # MEMO: たまに失敗しているため
      keystroke('searchField', 'Enter')
      wait_table('table1', '{0, コード}')
      doubleclick('table1', '{0, 名  称}')
      wait_table('table', '{0, コード}')
      click('部位'); sleep 0.1
      wait_table('table1', '{11, 名  称}')
      doubleclick('table1', '{11, 名  称}')
      wait_table('table', '{1, コード}')
      click('性質'); sleep 0.1
      wait_table('table1', '{22, 名  称}')
      doubleclick('table1', '{22, 名  称}')
      wait_table('table', '{2, コード}')
      click('病名'); sleep 0.1
      wait_table('table1', '{21, 名  称}')
      doubleclick('table1', '{21, 名  称}')
      wait_table('table', '{3, コード}')
      click('接尾'); sleep 0.1
      wait_table('table1', '{8, 名  称}')
      doubleclick('table1', '{8, 名  称}')
      wait_table('table', '{4, コード}')

      assert_p('table', 'Text', '皮膚', '{0, 疾患名/修飾語}')
      assert_p('table', 'Text', '皮膚', '{0, 疾患名/修飾語}')
      assert_p('table', 'Text', '顔', '{1, 疾患名/修飾語}')
      assert_p('table', 'Text', '感染性', '{2, 疾患名/修飾語}')
      assert_p('table', 'Text', 'カンジダ性', '{3, 疾患名/修飾語}')
      assert_p('table', 'Text', '後遺症', '{4, 疾患名/修飾語}')

      click('combinedDiagnosis')
      assert_p('stateLabel', 'Text', '基本傷病名がありません。')
      click('del_161')
      click('table', '{0, エイリアス}')
      click('del_161')
      click('table', '{0, 疾患名/修飾語}')
      click('del_161')
      click('table', '{0, 疾患名/修飾語}')
      click('del_161')
      click('table', '{0, 疾患名/修飾語}')
      click('del_161')
      select('searchField', 'おむつ')
      click('部位')
      select('searchField', '')
      select('findField', 'おむつ')
      doubleclick('table1', '{0, 名  称}')
      click('性質')
      doubleclick('table1', '{1, 名  称}')
      click('combinedDiagnosis')
      click('combinedDiagnosis')
      click('combinedDiagnosis')
      click('forwd_16')
      assert_p('StampTree', 'Text', 'おむつ皮膚炎.アトピー性', '{/傷病名/おむつ皮膚炎.アトピー性, おむつ皮膚炎.アトピー性}')
      select('StampTree', '[{/傷病名/おむつ皮膚炎.アトピー性, おむつ皮膚炎.アトピー性}]')
      window_closed('スタンプ箱-OpenDolphin')
      select('tools_24', 'false')
    }
  end

  on_inspector_for('00038', name) do |_, _, name|
    click('new_24')

    with_window('新規カルテ-OpenDolphin') {
      select('List', '[01270016  協会けんぽ]')
      select('List', '[01270016  協会けんぽ]')
      click('OK')
    }
  end

  with_window('スタンプ箱-OpenDolphin') {
    click('StampTree', '{/傷病名/おむつ皮膚炎.アトピー性, おむつ皮膚炎.アトピー性}')
    select('StampTree', '[{/傷病名/おむつ皮膚炎.アトピー性, おむつ皮膚炎.アトピー性}]')
    select('StampTree', '[{/傷病名/おむつ皮膚炎.アトピー性, おむつ皮膚炎.アトピー性}]')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師') {
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- カルテ | DolphinMara | 医師')
  }

  with_window('スタンプ箱-OpenDolphin') {
    select('StampTree', '[{/傷病名/おむつ皮膚炎.アトピー性, おむつ皮膚炎.アトピー性}]')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      select('tabbedPane', '傷病名')
      click('Table1', '{1, 疾患名/修飾語}')
    }

    select('StampTree', '[{/傷病名/おむつ皮膚炎.アトピー性, おむつ皮膚炎.アトピー性}]')

    with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
      click('Table', '{3, 分 類}')
      click('Table', '{0, 分 類}')
      click('Table', '{0, 分 類}')
      click('JTableHeader', '転 帰')
      click('Table', '{0, 転 帰}')
      click('Table', '{3, 疾患開始日}')
      click('Table', '{0, 疾患開始日}')
      assert_p('Table', 'Text', 'おむつ皮膚炎アトピー性', '{0, 疾患名/修飾語}')
      click('save_16')
      window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
    }
  }

  with_window('メインウインドウ-OpenDolphin | DolphinMara | 医師') {
    rightclick('RowTipsTable', '{1, 来院時間}')
    select_menu('カルテを開く')
  }

  with_window('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師') {
    select('tabbedPane', '傷病名')
    click('Table1', '{0, 分 類}')
    click('Table1', '{0, 疾患名/修飾語}')
    assert_p('Table1', 'Text', 'おむつ皮膚炎アトピー性', '{0, 疾患名/修飾語}')
    assert_p('Table1', 'Text', 'おむつ皮膚炎アトピー性', '{0, 疾患名/修飾語}')
    window_closed('ドルフィン マラソン(ドルフィン マラソン) : 00038- インスペクタ | DolphinMara | 医師')
  }


end

