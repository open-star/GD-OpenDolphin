
# Deplicate: on_inspector_for を使うべき
def on_karte_as(name, number = 0)
  with_window("メインウインドウ-OpenDolphin | #{name} | 医師") {
    select('TabbedPane', '患者検索')
    keystroke('TextField', 'Enter')
    # データベース依存
    # wait_p('AddressTipsTable', 'RowCount', '22')
    doubleclick('AddressTipsTable', "{#{number}, 氏名}")

    yield(name)
  }
end

