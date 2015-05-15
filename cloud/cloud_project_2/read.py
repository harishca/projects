import csv
import MySQLdb
from boto.s3.connection import S3Connection
from boto.s3.key import Key
import time

conn = S3Connection('AKIAJRFYSARNCQ2FGBHA','sZfZo5enehq2jWm3Iq3L7ongp/eyDQ4qXsL7Brti')
bucket = conn.create_bucket('harishkumarca')
k=Key(bucket)
k.key = 'myfile'
k.get_contents_to_filename('earthquake_details.csv')
conn.close()
con = MySQLdb.connect(host="harishkumarca.c52ndx7xtxxn.us-east-1.rds.amazonaws.com",user="harishkumarca",passwd="ahamBrahman$1",db="harishkumarca",local_infile=1)

cursor = con.cursor()

cursor.execute("DROP TABLE IF EXISTS PROJECT2") 
cursor.execute("CREATE TABLE PROJECT2(time timestamp,latitude float,longitude float,depth float,mag float,magtype varchar(255),nst int,gap float,dmin float,rms float,net varchar(255),id varchar(255),updated timestamp,place varchar(255),type varchar(255))")
cursor.execute("""load data local infile 'earthquake.csv' into table PROJECT2 fields terminated by ',' enclosed by '\"' linse terminated by '\n' ignore 1 lines;""")
cursor.execute("commit")

#with open('downlaoded.csv','rb') as f:
    #reader = csv.reader(f)
    #first_line=f.readline()
    #print first_line
    ##for row in reader:
    #    print row
con.close()
print "done";
