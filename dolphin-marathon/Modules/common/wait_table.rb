
def wait_table(table_name, info = nil, pattern = nil)
  if info
    if pattern
      wait_table_pattern table_name, info, pattern
    else
      wait_table_columns table_name, info
    end
  else
    wait_table_rows table_name
  end
end

def wait_table_rows(table_name)
  start = Time.now
  until get_p(table_name, 'RowCount').to_i > 0
    sleep 0.01
    raise "Couldn't wait: #{table_name}" if (Time.now - start) > 30
  end
end

def wait_table_columns(table_name, info)
  start = Time.now
  while get_p(table_name, 'Text', info).empty? 
    sleep 0.01
    raise "Couldn't wait: #{table_name}" if (Time.now - start) > 30
  end
end

def wait_table_pattern(table_name, info, pattern)
  until pattern =~ get_p(table_name, 'Text', info)
    sleep 0.1
  end
end

