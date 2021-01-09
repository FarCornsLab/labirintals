from PlayerObject import PlayerObject
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

from MazeBlock import MazeBlock
from PlayerObject import PlayerObject
from AimObject import AimObject

class GameScene(Scene):
    def __init__(self):
        super().__init__(pygame.Surface((10,10)))
        self._create_menu()
        self.cur_maze_block = None
        self.player = PlayerObject(120,120)
        self.objects.append(self.player)
        self.custom_event_handlers[pygame.MOUSEWHEEL].append(self.mouse_wheel_event)
        self.custom_event_handlers[pygame.MOUSEBUTTONDOWN].append(self.mouse_button_down)
        self.custom_event_handlers[pygame.MOUSEBUTTONUP].append(self.mouse_button_up)
        self.custom_event_handlers[pygame.MOUSEMOTION].append(self.mouse_motion)
        self.mouse_but_state = {1:False,2:False}
        self.aims = []
        self.aims_start_num = -1
        self.cur_step_id = -1
        self.next_step_time = -100
        self.make_step()
        self.sec_timer_event = pygame.event.custom_type()
        self.custom_event_handlers[self.sec_timer_event].append(self.sec_timer_event_handler)
        pygame.time.set_timer(self.sec_timer_event,1000)

    def send_step(self,direction):
        self.next_step  = direction
        self.player.rot = {"up":0,"down":180,"left":270,"right":90}[self.next_step]
        Core.core.net_manager.send_cmd("make_step",{"step_id":self.cur_step_id,"step_type":self.next_step})
        self.cur_step_answer = Core.core.net_manager.recv_answer()
        #self.next_step_time = self.cur_step_answer["params"]["next_step_time"]

    def make_step(self):
        self.request_maze_position()
        if self.cur_step_id == self.cur_maze_position["params"]["step_id"]:
            return
        self.cur_step_id = self.cur_maze_position["params"]["step_id"] 
        if self.aims_start_num != -1:
            for i in range(len(self.aims)):
                self.objects.pop(self.aims_start_num)
        self.aims.clear()
        direction_to_point = {"up":(0,100),"down":(0,-100),"left":(-100,0),"right":(100,0)}
        if self.cur_maze_block == None:
            self.cur_maze_block = MazeBlock(self.cur_maze_position["params"]["field_unit"],
                                        200 ,
                                        200)
        else:
            self.cur_maze_block = MazeBlock(self.cur_maze_position["params"]["field_unit"],
                                        self.cur_maze_block.position[0] + direction_to_point[self.next_step][0] ,
                                        self.cur_maze_block.position[1] + direction_to_point[self.next_step][1])
        self.objects.insert(0,self.cur_maze_block)
        if self.cur_maze_position["params"]["field_unit"][0] != "obstacle":
            self.aims.append(AimObject(self.cur_maze_block.position[0],self.cur_maze_block.position[1] - self.cur_maze_block.size[1],name ="up"))

        if self.cur_maze_position["params"]["field_unit"][1] != "obstacle":
            self.aims.append(AimObject(self.cur_maze_block.position[0] + self.cur_maze_block.size[1],self.cur_maze_block.position[1],name ="right"))

        if self.cur_maze_position["params"]["field_unit"][2] != "obstacle":
            self.aims.append(AimObject(self.cur_maze_block.position[0], self.cur_maze_block.position[1] + self.cur_maze_block.size[1],name ="down"))

        if self.cur_maze_position["params"]["field_unit"][3] != "obstacle":
            self.aims.append(AimObject(self.cur_maze_block.position[0] - self.cur_maze_block.size[1],self.cur_maze_block.position[1],name ="left"))
        self.aims_start_num = len(self.objects)
        self.objects.extend(self.aims)
        self.player.set_center(self.cur_maze_block.get_center())

    def mouse_button_down(self,event):
        self.mouse_but_state[event.button] = True
        gl_point  = self.screen_point_to_global(event.pos)
        for aim in self.aims:
             if aim.colidepoint(gl_point):
                 self.send_step(aim.name)
                 for aim2 in self.aims:
                    aim2.picked = False
                 aim.picked = True
    
    def mouse_button_up(self,event):
        self.mouse_but_state[event.button] = False

    def mouse_motion(self, event):
        if self.mouse_but_state[1] == True:
            self.camera_offset = (self.camera_offset[0]+event.rel[0],self.camera_offset[1]+event.rel[1])

    def mouse_wheel_event(self,event):
        self.zoom = self.zoom + 0.1 * event.y

    def request_maze_position(self):
        Core.core.net_manager.send_cmd("get_position")
        self.cur_maze_position = Core.core.net_manager.recv_answer()
        #self.next_step_time = self.cur_maze_position["params"]["next_step_time"]
    #    self.cur_maze_position = {"field_unit":[
    #         "obstacle",
    #         "free",
    #         "obstacle",
    #         "exit"
    #     ]}

    def _create_menu(self):
        w,h = pygame.display.get_surface().get_size()
        panel_size = (600,100)
        self.panel = pygame_gui.elements.UIPanel(pygame.Rect((w/2 - panel_size[0]/2,h -panel_size[1]),panel_size),
                                        starting_layer_height=4,
                                        manager=self.ui_manager)
        self.btn_disconect = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((panel_size[0]-110,5),(100,25)),
                                            text='Disconnect',
                                            container=self.panel,
                                            manager=self.ui_manager)
        
        self.btn_pressed_call_backs.append((self.btn_disconect,self.btn_disconect_pressed))
        self.next_step_timer_label = pygame_gui.elements.UILabel(
                        relative_rect=pygame.Rect(5, 5, 300, 50),
                        text="Time to next step ", 
                        manager=self.ui_manager,
                        container=self.panel,
                        )
    
    def sec_timer_event_handler(self,event):
        self.time_left = int(int(self.next_step_time)/1000 -time.time())
        self.next_step_timer_label.set_text("Time to next step "+ str(self.time_left))
        if self.time_left <= 0 :
            pass
            #self.make_step()

    def btn_disconect_pressed(self, event):
        Core.core.net_manager.disconnect()
        Core.core.load_scene("FindServerMenu")
