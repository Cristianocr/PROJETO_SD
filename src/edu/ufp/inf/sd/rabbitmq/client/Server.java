package edu.ufp.inf.sd.rabbitmq.client;

/*public class Server {


   Server(int portNumber) {
      ServerSocket ss;

      try {
         System.out.print("Abrindo a porta " + portNumber + "...");
         ss = new ServerSocket(portNumber); // socket escuta a porta
         System.out.print(" ok\n");

         for (int id = 0; !loggedIsFull(); id = (++id)% Const.QTY_PLAYERS)
            if (!player[id].logged) {
               Socket clientSocket = ss.accept();
               new ClientManager(clientSocket, id).start();
            }
         //nao encerra o servidor enquanto a thread dos clientes continuam executando
      } catch (IOException e) {
         System.out.println(" erro: " + e + "\n");
         System.exit(1);
      }
   }

   boolean loggedIsFull() {
      for (int i = 0; i < Const.QTY_PLAYERS; i++)
         if (player[i].logged == false)
            return false;
      return true;
   }



   public static void main(String[] args) {
      new Server(8383);
   }
}*/