import pygame
import sys
from collections import defaultdict
from pygame import surface

from pygame_gui import ui_manager
from Scene import Scene
import Core
import os
import pygame_gui

class FindServerMenu(Scene):
    def __init__(self):
        super().__init__(pygame.image.load(Core.resource_path(os.path.join('images','MainMenu_backgraund.png'))).convert())
        self._create_menu()

    def _create_menu(self):
        w,h = pygame.display.get_surface().get_size()
        panel_size = (600,420)
        
        self.panel = pygame_gui.elements.UIPanel(pygame.Rect((w/2 - panel_size[0]/2,h/2 -panel_size[1]/2),panel_size),
                                        starting_layer_height=4,
                                        manager=self.ui_manager)
        self.btn_back = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((5,5),(50,25)),
                                            text='Back',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_pressed_call_backs.append((self.btn_back,self.btn_back_pressed))
        self.btn_connect = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((50,350),(100,25)),
                                            text='Connect',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_pressed_call_backs.append((self.btn_connect,self.btn_connect_pressed))

        self.btn_direct_ip = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((430,350),(120,25)),
                                            text='Connect by IP',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_pressed_call_backs.append((self.btn_direct_ip,self.btn_direct_ip_pressed))

        self.txt_ip = pygame_gui.elements.UITextEntryLine( 
                                            relative_rect=pygame.Rect((280,350),(150,25)),
                                            container=self.panel,
                                            manager=self.ui_manager)

        sel_item_list  = [str(i+1)+". "+Core.core.config["server_list"][i]["name"] for i in range(len(Core.core.config["server_list"]))]
        self.server_list = pygame_gui.elements.UISelectionList(pygame.Rect(50, 50, 500, 300),
                        item_list=sel_item_list,
                        manager=self.ui_manager,
                        container=self.panel)

        self.observer_check = pygame_gui.elements.UISelectionList(pygame.Rect((50,380),(120,26)),
                        item_list=["As observer"],
                        manager=self.ui_manager,
                        container=self.panel)

        self.txt_observer_token = pygame_gui.elements.UITextEntryLine( 
                                            relative_rect=pygame.Rect((170,380),(300,25)),
                                            container=self.panel,
                                            manager=self.ui_manager)
        

    def btn_back_pressed(self, event):
        Core.core.load_scene("MainMenu")

    def btn_direct_ip_pressed(self, event):
        txt = self.txt_ip.get_text().split(":")
        self.connect_to(txt[0],int(txt[1]))

    def connect_to(self,ip,port):
        observer_check_val = self.observer_check.get_single_selection()
        try:
            if(observer_check_val != None):
                Core.core.net_manager.connect(ip,port,"observer",self.txt_observer_token.get_text())
            else:
                Core.core.net_manager.connect(ip,port)
            Core.core.load_scene("WaitStartGameMenu")
        except ConnectionError as err:
                print("Error"+ err.strerror)
                self.err_msg = pygame_gui.windows.UIMessageWindow(
                    rect=pygame.Rect(100, 100, 350, 200),
                    html_message="Connection Error <br>"+ err.strerror,
                    manager=self.ui_manager,
                    window_title="Error")

    def btn_connect_pressed(self, event):
        sel = self.server_list.get_single_selection()
        if sel != None:
            print("Connect to "+ sel )
            num = int(sel.split(sep=".")[0]) - 1
            server = Core.core.config["server_list"][num]
            self.connect_to(server["ip"],server["port"])
            