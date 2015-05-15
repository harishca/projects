from boto.s3.connection import S3Connection
from boto.s3.key import Key
import time

conn = S3Connection('AKIAJRFYSARNCQ2FGBHA','sZfZo5enehq2jWm3Iq3L7ongp/eyDQ4qXsL7Brti')
bucket = conn.create_bucket('harishkumarca')
k=Key(bucket)
k.key = 'myfile'
start = time.time()
k.set_contents_from_filename('titanic.csv')
k.get_contents_to_filename('downlaoded_titanic.csv')
end = time.time()
print end-start
conn.close()

