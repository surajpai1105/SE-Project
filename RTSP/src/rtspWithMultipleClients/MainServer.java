package rtspWithMultipleClients;

public class MainServer
{
	public static void main(String[] args)
	{
		final ServerConnectionAcceptor startServer = new ServerConnectionAcceptor();
		startServer.acceptConnections();
//		try
//		{
//			Thread.sleep(6000);
//		}
//		catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		new Thread(new Runnable()
//		{
//			public void run()
//			{
//				startServer.acceptConnections();
//				Client startClient = new Client();
//				try
//				{
//					Thread.sleep(3000);
//				}
//				catch (InterruptedException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				// startClient.play();
//			}
//		}).start();
//
////		Client startClient2 = new Client();
////
////		startClient2.play();
	}
}