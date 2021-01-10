import pygame
import sys
import random
import time
from collections import defaultdict

from pygame_gui import ui_manager
from Scene import Scene
import Core
import os
import pygame_gui
import pygame

class WaitStartGameMenu(Scene):
    def __init__(self):
        super().__init__(pygame.image.load(Core.resource_path(os.path.join('images','MainMenu_backgraund.png'))).convert())
        self.max_players = 4
        self.game_params = Core.core.net_manager.game_params
        self.start_time = 0
        self.update_game_state_event = pygame.event.custom_type()
        self.custom_event_handlers[self.update_game_state_event].append(self.update_game_state)
        self.player_list = self.game_params["players"]
        self._create_menu()
        pygame.time.set_timer(self.update_game_state_event,1000)

    def _create_menu(self):
        w,h = pygame.display.get_surface().get_size()
        panel_size = (600,400)
        self.panel = pygame_gui.elements.UIPanel(pygame.Rect((w/2 - panel_size[0]/2,h/2 -panel_size[1]/2),panel_size),
                                        starting_layer_height=4,
                                        manager=self.ui_manager)
        self.btn_disconect = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((5,5),(100,25)),
                                            text='Disconnect',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_pressed_call_backs.append((self.btn_disconect,self.btn_disconect_pressed))
        
        self.player_list_ui = pygame_gui.elements.UISelectionList(pygame.Rect(250, 50, 300, 200),
                        item_list=[],
                        manager=self.ui_manager,
                        container=self.panel)
        self.start_timer_label = pygame_gui.elements.UILabel(
                        relative_rect=pygame.Rect(20, 50, 200, 50),
                        text="Wait players", 
                        manager=self.ui_manager,
                        container=self.panel,
                        )
        self.update_game_state(None)

    def update_game_state(self,event):
        Core.core.net_manager.update_game_params()
        self.game_params = Core.core.net_manager.game_params
        self.player_list = self.game_params["players"]
        sel_item_list  = [str(i+1)+". "+self.player_list[i]["name"] for i in range(len(self.player_list))]
        if len(sel_item_list) < self.max_players:
            for i in range(self.max_players - len(sel_item_list)):
                sel_item_list.append("Finding players" + ("."*random.randint(0,4)))
        self.player_list_ui.set_item_list(sel_item_list)

        if self.game_params["start_time"] == 0:
            self.start_time = 100
            self.start_timer_label.set_text("Wait players" + ("."*random.randint(0,4)))
        else:
            self.start_time = int(int(self.game_params["start_time"])/1000 -time.time())
            self.start_timer_label.set_text("Game starts in " + str(max(self.start_time,0)) + " sec" )
            if self.start_time < 0:
                Core.core.net_manager.send_cmd("get_position")
                maze_position = Core.core.net_manager.recv_answer()
                if maze_position["params"]["step_id"] > 0:
                    Core.core.load_scene("GameScene")


    def btn_disconect_pressed(self, event):
        Core.core.net_manager.disconnect()
        Core.core.load_scene("FindServerMenu")
