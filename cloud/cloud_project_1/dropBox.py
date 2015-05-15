import gnupg
import dropbox
import os
import urllib2

def dropbox_authorization():
    #assigning application key and secret
    app_key = "uios56x8g7vlp0b"
    app_secret = "85uwxbvnm3s7sga"
    flow = dropbox.client.DropboxOAuth2FlowNoRedirect(app_key,app_secret)
    access_token='xR8yqGdOY1sAAAAAAAAD_E3wFIjBqyLpRVx_ApyqSieqTIfxHfKeEUGgqB01c5Kb'
    client = dropbox.client.DropboxClient(access_token)
    print 'linked account: ', client.account_info()
    return client

def encryptAndDecrypt(filename):
    client=dropbox_authorization()
    gpg = gnupg.GPG()
    head ,tail = os.path.split(filename)
    with open(filename, 'rb') as f:
        status = gpg.encrypt_file(f, recipients=['puneeth.shopping@gmail.com'],
            output=tail,always_trust=True)
    
    f = open(tail, 'rb')
    #uploading the files to dropbox
    response = client.put_file(tail, f,overwrite=True)
    f.close()
    print 'ok: ', status.ok
    print 'status: ', status.status
    print 'stderr: ', status.stderr
    f, metadata = client.get_file_and_metadata(tail)

    #downloading files from dropbox
    status = gpg.decrypt_file(f,passphrase='harish123', output=tail)
    f.close()
    print 'ok: ', status.ok
    print 'status: ', status.status
    print 'stderr: ', status.stderr

#this code works for the folder version of the application.
def deleteFileDropbox(filename):
    client=dropbox_authorization()
    head ,tail = os.path.split(filename)
    client.file_delete(tail)

def signAndVerify(filename):
    client=dropbox_authorization()
    gpg = gnupg.GPG()
    head ,tail = os.path.split(filename) 
    print('Signing the document')
    f=open(filename,'rb')
    signed_data=gpg.sign_file(f,detach=True,output=tail+'sig',keyid='harishcakumar@gmail.com')
    x=open(filename,'rb')
    y=open(tail+'sig','rb')
    response = client.put_file(tail, x)
    response1 = client.put_file(tail+'sig',y)
    folder_metadata = client.metadata('/')
    print 'metadata: ', folder_metadata
    f, metadata = client.get_file_and_metadata(tail)
    g, metadata = client.get_file_and_metadata(tail+'sig')
    j=open(tail,'wb')
    j.write(f.read())
    j.close()
    k=open(tail+'sig','wb')
    k.write(g.read())
    k.close()
    l=open(tail+'sig','rb')
    verified = gpg.verify_file(l,filename)
    print "Verified" if verified else "Unverified"

