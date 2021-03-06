import socket
import json
import asyncio

class NetworkManager: 
    def __init__(self,player_name):
        self.sock = socket.socket()
        self.buf_size = 4096
        self.player_name = player_name
        self.oid = 0
        self.client_type = "gamer"
        #self.cid = 0
        self.game_params = {}
        pass

    def connect(self,ip,port, client_type="gamer",token=""):
        self.client_type = client_type
        self.sock = socket.socket()
        self.sock.setblocking(True)
        self.sock.connect((ip,port))
        try:
            if client_type == "observer":
                self.send_cmd(cmd ="connection",params ={"name":self.player_name,"token":token})
            else:
                self.send_cmd(cmd ="connection",params ={"name":self.player_name})
            ans  = self.recv_answer()
            if("error" in ans):
                err = ConnectionError(ans["error"]["message"])
                err.strerror = ans["error"]["message"]
                raise err
            if client_type != "observer":
                self.oid = ans["params"]["player"]["oid"]
            #self.cid = ans["params"]["cid"]
            self.update_game_params()
        except Exception as err:
             self.sock.close()
             raise err
        return True

    def disconnect(self):
        try:
            self.send_cmd("disconnect")
            print(self.recv_answer())
            self.sock.shutdown(socket.SHUT_RDWR)
            self.sock.close()
        except:
            pass

    def send_cmd(self,cmd,params = None):
        if params == None:
            json_cmd = {"cmd":cmd}
        else:
            json_cmd = {"cmd":cmd,"params":params}
        json_str = json.dumps(json_cmd)
        json_str += "\r\n"
        bytes = json_str.encode()
        self.sock.sendall(bytes)
    
    def recv_answer(self):
        json_str = ""
        buf = self.sock.recv(self.buf_size)
        json_str += buf.decode()
        while len(buf) > 0 and buf[len(buf)-1] != ord("\n"):
            buf = self.sock.recv(self.buf_size)
            if(len(buf) > 0):
                json_str += buf.decode()
        return json.loads(json_str)

    def update_game_params(self):
        self.send_cmd(cmd ="get_game_params")
        ans  = self.recv_answer()
        self.game_params = ans["params"]

