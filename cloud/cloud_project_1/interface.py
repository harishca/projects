import Tkinter
import tkFileDialog
import enc
import gd

root = Tkinter.Tk()
#root.withdraw()
root.geometry('600x300')
root.wm_title('Share in the Cloud')
var='Hi there please select one of the options displayed below'
def encrypt():
        child = Tkinter.Tk()
        child.geometry('300x150')
        child.wm_title('Encrytion')
        CheckVar1 = Tkinter.IntVar()
        C1 = Tkinter.Radiobutton(child,text="Dropbox",variable=CheckVar1,value=1,command=dropboxupload_encrypt)
        C2 = Tkinter.Radiobutton(child,text="Google Drive",variable=CheckVar1,value=2,command=googledriveupload_encrypt)
        C1.pack()
        C2.pack()
        child.mainloop()
def sign():
        child = Tkinter.Tk()
        child.geometry('300x150')
        child.wm_title('Sign')
        CheckVar1 = Tkinter.IntVar()
        CheckVar2 = Tkinter.IntVar()
        C1 = Tkinter.Radiobutton(child,text="Dropbox",variable=CheckVar1,value=1,command=dropboxupload_sign)
        C2 = Tkinter.Radiobutton(child,text="Google Drive",variable=CheckVar1,value=2,command=googledriveupload_encrypt)
        C1.pack()
        C2.pack()
        child.mainloop() 
def dropboxupload_encrypt():
    filename = tkFileDialog.askopenfilename(parent=root,title='Open file to encrypt')
    enc.encryptAndDecrypt(filename)
def dropboxupload_sign():
    filename = tkFileDialog.askopenfilename(parent=root,title='Open file to sign')
    enc.signAndVerify(filename)
def googledriveupload_encrypt():
    filename = tkFileDialog.askopenfilename(parent=root,title='Open file to encrypt')
    gd.upload_google(filename)
    
      
label = Tkinter.Label(text=var,foreground="black",background="white")

B = Tkinter.Button(root,text="Encrypt and Unpload",width=30,height=5,command=encrypt)
D = Tkinter.Button(root,text="Sign and Upload",width=30,height=5,command=sign)
label.pack() 
B.pack()
D.pack()
root.mainloop()
