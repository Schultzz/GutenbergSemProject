#!/bin/sh
echo "setup schema"
mysql -uroot -ppwd -e 'DROP DATABASE IF EXISTS gutenberg;'
mysql -uroot -ppwd -e 'CREATE DATABASE gutenberg;'
mysql -uroot -ppwd gutenberg < /synced_folder/vm/mysqlsetup/mysqlSchema.sql
echo "Inserting author.csv"
mysqlimport -uroot -ppwd --fields-terminated-by=','  --fields-enclosed-by='"' --lines-terminated-by='\n' -L gutenberg /synced_folder/vm/mysqlsetup/author.csv
echo "Inserting book.csv"
mysqlimport -uroot -ppwd --fields-terminated-by=','  --fields-enclosed-by='"' --lines-terminated-by='\n' -L gutenberg /synced_folder/vm/mysqlsetup/book.csv
echo "Inserting location.csv"
mysql -uroot -ppwd gutenberg < /synced_folder/vm/mysqlsetup/location.sql
echo "Inserting author_book_join.csv"
mysqlimport -uroot -ppwd --fields-terminated-by=','  --fields-enclosed-by='"' --lines-terminated-by='\n' -L gutenberg /synced_folder/vm/mysqlsetup/author_book_join.csv
echo "Inserting location_book_join.csv"
mysqlimport -uroot -ppwd --fields-terminated-by=','  --fields-enclosed-by='"' --lines-terminated-by='\n' -L gutenberg /synced_folder/vm/mysqlsetup/location_book_join.csv
