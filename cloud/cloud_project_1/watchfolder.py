import time
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler
import enc
import os
import glob

class MyHandler(FileSystemEventHandler):
    def on_deleted(self, event):
        file = event.src_path
        head ,tail = os.path.split(file)
        print ("Deleted file is : " + tail)
        enc.deleteFileDropbox(file)
    def on_created(self, event):
        file = event.src_path
        head ,tail = os.path.split(file)
        print ("Created file is : " + tail)
        enc.encryptAndDecrypt(file)
if __name__ == "__main__":
    event_handler = MyHandler()
    observer = Observer()
    observer.schedule(event_handler, path='/Users/hari/Desktop/Dropbox', recursive=False)
    observer.start()

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()
