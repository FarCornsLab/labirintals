import socket
import json

class NetworkManager: 
    def __init__(self,player_name):
        self.sock = socket.socket()
        self.buf_size = 4096
        self.player_name = player_name
        self.oid = 0
        self.cid = 0
        self.game_params = {}
        pass

    def connect(self,ip,port):
        self.sock.connect((ip,port))
        self.send_cmd(cmd ="connection",params ={"name":self.player_name})
        ans  = self.recv_answer()
        self.oid = ans["oid"]
        self.cid = ans["cid"]
        self.update_game_params()
        return True

    def send_cmd(self,cmd,params):
        json_cmd = {"cmd":cmd,"params":params}
        json_str = json.dumps(json_cmd)
        bytes = json_str.encode()
        self.sock.sendall(bytes)
    
    def recv_answer(self):
        json_str = ""
        buf = self.sock.recv(self.buf_size)
        json_str += buf.decode()
        while len(buf) > 0:
            buf = self.sock.recv(self.buf_size)
            if(len(buf) > 0):
                json_str += buf.decode()
        return json.loads(json_str)

    def update_game_params(self):
        self.send_cmd(cmd ="get_game_params",params ={})
        ans  = self.recv_answer()
        self.game_params = ans["params"]

