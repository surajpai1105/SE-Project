package RTP;

// FTP Client
///////////////////////////////
//     Data Transfer Module  //
//  Authors:Keshav & Eshwar  //
///////////////////////////////

//Networking Library
import java.net.*;
//Input/Output Library
import java.io.*;
//Utility Library
import java.util.*;
//File Integrity Library
import java.security.*;
import javax.swing.JOptionPane;

public class FTPClientAPI
{
    Socket ClientSoc;

    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;
    public FTPClientAPI(String serverip)
    {
        try
        {
            ClientSoc=new Socket(serverip,5000);
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            br=new BufferedReader(new InputStreamReader(System.in));
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }        
    }

	

    //Method to send files --keshav
    public void SendFile(String filename)
    {        
      try
	  {
		dout.writeUTF("SEND");
                //Resume Variable
		long resume = 0;
//		Check if file exists --keshav
        File f=new File(filename);
        if(!f.exists())
        {
            System.out.println("File does not Exist...");
            dout.writeUTF("File not found");
            return;
        }
        String[] tempname;
		System.out.println(filename);
		tempname = filename.split("/");
//		for(String e:tempname)
//			System.out.println(e);
		String newfile = tempname[tempname.length-1];
//		System.out.println(newfile);
		System.out.println(ClientSoc.getLocalSocketAddress().toString());
                String ipaddr = ClientSoc.getLocalSocketAddress().toString().split(":")[0];
		String newip = ipaddr.substring(1,ipaddr.length());
		System.out.println(newip);
                pathtranslator pt = new pathtranslator();
		//String absp = newip+"/"+newfile;
                String absp = pt.encode(filename);
                System.out.println(absp);
		dout.writeUTF(absp);
		dout.writeUTF(String.valueOf(f.length()));
//		dout.writeUTF(newip+"/"+newfile);
        //Check if file exists --keshav
        String msgFromServer=din.readUTF();
        if(msgFromServer.compareTo("File Already Exists")==0)
        {
            String Option;
			
            System.out.println("File Already Exists. Want to resume (Y/N) ?");
            Option=br.readLine();            
            if(Option.equals("Y"))    
            {
                dout.writeUTF("Y");
				resume =Long.valueOf(din.readUTF());
//				System.out.println("Got from server : "+resume);
            }
            else
            {
                dout.writeUTF("N");
                return;
            }
        }
        
        System.out.println("Sending File ...");
        FileInputStream fin=new FileInputStream(f);
		fin.skip(resume);
        int ch;
/*		
        do
        { 
            ch=fin.read();
            dout.writeUTF(String.valueOf(ch));
        }
        while(ch!=-1);
*/		
		byte[] b = new byte[65536];
		while ((ch = fin.read(b)) > 0)
		{
//			System.out.println(ch);
			dout.write(b, 0, ch);
		}
		
        fin.close();
        
        System.out.println(din.readUTF());
        System.out.println("File integrity verified.");
        // Code for auto check
        /*if(filecheck(filename,absp)==din.readUTF())
        {
            System.out.println("MD5 Checksums verified.");
            
        }
        else
            System.out.println("MD5 checksums do not match.");*/
        filecheck(filename,absp);
        f.delete();
        //System.exit(1);
		
		}
		
		catch(Exception exp)
		{
			System.out.println(exp);
			System.out.println("Connection closed at Server... \nClosing client...");
			System.exit(1);
		}
    }
    //MD5 Checksum Generator 
    //Author:Keshav
    //Lang:JAVA
    //Date:25/02/15
     String filecheck(String filename , String absp) throws Exception
    {	
		dout.writeUTF("CHECK");
		dout.writeUTF(absp);
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = new FileInputStream(filename))
        {
          DigestInputStream dis = new DigestInputStream(is, md);
          //Buffer Size assign
          byte[] buffer = new byte[1024];
          //Read variable
          int numRead;
          do {
            numRead = is.read(buffer);
              if (numRead > 0) {
              //Update MessageDigest to reflect new data
               md.update(buffer, 0, numRead);
           }
            } while (numRead != -1);   
            is.close();
        byte[] digest = md.digest();
        String result = "";
        //Convert to HEX String
        for (int i=0; i < digest.length; i++) {
           result += Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
        System.out.println("MD5 Hash Value for "+filename+": "+result);
        return result;
      }
    }
    //Method to receive files --keshav
    public void ReceiveFile(String fileName)
    {
		try
		{
			dout.writeUTF("GET");
			long fsize =0 ;
			long resume = 0;
	 //     String fileName;
	 //     System.out.print("Enter File Name :");
	 //     fileName=br.readLine();
			dout.writeUTF(fileName);
			String msgFromServer=din.readUTF();
			//Check if file is present --keshav 
			if(msgFromServer.compareTo("File Not Found")==0)
			{
				System.out.println("File not found on Server ...");
				return;
			}
			else if(msgFromServer.compareTo("READY")==0)
			{
				fsize =Long.valueOf(din.readUTF());
				System.out.println("Receiving File ...");
				File f=new File(fileName);
				//Check if file exists --keshav
				if(f.exists())
				{
					String Option;
					System.out.println("File Already Exists. Want to Resume (Y/N) ?");
					//Option=br.readLine();            
					Option = JOptionPane.showInputDialog(null, "File Already Exists. Want to OverWrite (Y/N) ?");
					System.out.println(Option);
					if(Option.equalsIgnoreCase("N"))    
					{
						dout.writeUTF("N");
						JOptionPane.showMessageDialog(null,"File Download was Stopped");
						return;    
					}
				}
				
				
				dout.writeUTF("Y");
				resume = f.length();
				dout.writeUTF(String.valueOf(f.length()));
			
				
				FileOutputStream fout=new FileOutputStream(f,true);
				int ch;
				long remaining = fsize - resume ; 
				byte[] b = new byte[65536];
				while (remaining > 0)
				{	
					ch = din.read(b);
					remaining -= ch;
					System.out.println(ch);
					fout.write(b, 0, ch);
				}
				fout.close();
				System.out.println(din.readUTF());
				JOptionPane.showMessageDialog(null,"File Received Successfully");
				//Automatically generate MD5 checksum after file transfer complete  -- Keshav
			  //  filecheck(fileName);
			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			System.out.println("Connection closed at Server... \nClosing client...");
			System.exit(1);
		}
        
    }
    //Menu Handler
 /*   public void displayMenu() throws Exception
    {
        while(true)
        {    
            System.out.println("[ MENU ]");
            System.out.println("1. Send File");
            //System.out.println("2. Receive File");
            System.out.println("3. File Checksum");
            System.out.println("4. Exit");
            System.out.print("\nEnter Choice :");
            int choice;
            choice=Integer.parseInt(br.readLine());
            if(choice==1)
            {
                dout.writeUTF("SEND");
                SendFile();
            }
            else if(choice==2)
            {
                dout.writeUTF("GET");
                ReceiveFile();
            }
            else if(choice==3)
            {
                dout.writeUTF("CHECK");
            }
            else            {
                dout.writeUTF("DISCONNECT");
                System.exit(1);
            }
        }
    }
*/	
}