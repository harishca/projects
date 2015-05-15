import httplib2
import pprint
from apiclient.discovery import build
from apiclient.http import MediaFileUpload
from oauth2client.client import OAuth2WebServerFlow
import os
import gnupg

def upload_google(filename):
    # Copy your credentials from the console
    CLIENT_ID = '346086845317-ncos9oc2uoln5plqdpvaidl8dvu1goiq.apps.googleusercontent.com'
    CLIENT_SECRET = 'LhwOLNPo3A67JZLqHitDn3fO'
    gpg = gnupg.GPG()
    # Check https://developers.google.com/drive/scopes for all available scopes
    OAUTH_SCOPE = 'https://www.googleapis.com/auth/drive'

    # Redirect URI for installed apps
    REDIRECT_URI = 'urn:ietf:wg:oauth:2.0:oob'

    # Path to the file to upload
    # FILENAME = 'document.txt'

    # Run through the OAuth flow and retrieve credentials
    flow = OAuth2WebServerFlow(CLIENT_ID, CLIENT_SECRET, OAUTH_SCOPE,
                                   redirect_uri=REDIRECT_URI)
    authorize_url = flow.step1_get_authorize_url()
    print 'Go to the following link in your browser: ' + authorize_url
    code = raw_input('Enter verification code: ').strip()
    credentials = flow.step2_exchange(code)

    # Create an httplib2.Http object and authorize it with our credentials
    http = httplib2.Http()
    http = credentials.authorize(http)

    service = build('drive', 'v2', http=http)

    # Insert a file 
    head ,tail = os.path.split(filename)
    print ('Filename selected : '+tail)
    with open(filename,'rb') as f:
        status = gpg.encrypt_file(
                f,recipients=['harishcakumar@gmail.com'],
                output=tail)
    print 'ok', status.ok
    print 'status', status.status
    print 'stderr', status.stderr
    media_body = MediaFileUpload(tail, mimetype='text/plain', resumable=True)
    body = {'title': tail,'description': 'A test document','mimeType': 'text/plain'}

    drive_file = service.files().insert(body=body, media_body=media_body).execute()
    print '!!!!uploaded!!!!!'
    pprint.pprint(drive_file)
    download_google(service, drive_file,tail)

def download_google(service, drive_file,tail):
    # Download a file
    gpg = gnupg.GPG()
    print 'downloading file'
    download_url = drive_file.get('downloadUrl')
    print 'download url :',download_url
    if download_url:
        resp,content = service._http.request(download_url)
        if resp.status==200:
            #print 'status:%s'%resp
            #print content
            x=open('intermediate','wb')
            x.write(content)
            x.close()
            x=open('intermediate','rb')
            status = gpg.decrypt_file(x,passphrase='harish123',output=tail)
            x.close()
            print 'ok : ', status.ok
            print 'status: ', status.status
            print 'stderr: ', status.stderr
            return content
        else:
            print 'An error occured: %s' %resp
            return None
    else:
        return None
   
