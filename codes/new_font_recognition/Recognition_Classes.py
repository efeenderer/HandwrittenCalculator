import os
import numpy as np
import cv2 
import time as t
import json

class Extractor:
    def __init__(self, image_path):
        self.path = os.path.dirname(image_path)

        self.img = cv2.imread(image_path,cv2.IMREAD_GRAYSCALE)
        self.characterImages = list()

        self.Extraction()
    
    def Extraction(self):

        _,thresh_img = cv2.threshold(self.img, 127,255, cv2.THRESH_BINARY_INV) #+ cv2.THRESH_OTSU)
        #IMAGE_Y,IMAGE_X = thresh_img.shape[:2]
        contours , _ = cv2.findContours(thresh_img,cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_NONE)

        self.characterContours = list()

        for index ,contour in enumerate(contours):
            min_x = min(point[0][0] for point in contour)
            max_x = max(point[0][0] for point in contour)
            min_y = min(point[0][1] for point in contour)
            max_y = max(point[0][1] for point in contour)

            contour_image_width = max_x - min_x + 1
            contour_image_height = max_y - min_y + 1

            contour_image = np.zeros((contour_image_height, contour_image_width), dtype=np.uint8)
            
            for point in contour:
                x, y = point[0]
                contour_image[y - min_y, x - min_x] = 255

            x, y, w, h = cv2.boundingRect(contour)
            
            char = thresh_img[y:y+h, x:x+w]

            """
            ####################   DOT CHECKER   ##################################
            for i,check_contour in enumerate(contours):
                
                check_x, check_y, check_w, check_h = cv2.boundingRect(check_contour)

                dot_check_y1 = y - int(h/2) - 5
                dot_check_y2 = y - 1

                dot_check_x1 = x - 5
                dot_check_x2 = x + w + 5

                if dot_check_x1 < 0 or dot_check_x2 >= IMAGE_X or dot_check_y1 < 0 or dot_check_y2 >= IMAGE_Y:
                    #print(f"OUT OF BORDERS {(dot_check_x1,dot_check_x2,dot_check_y1,dot_check_y2)}")
                    continue
                
                #t.sleep(0.8)
                
                if check_y >= dot_check_y1 and check_y < dot_check_y2:
                    if check_x >= dot_check_x1 and check_x < dot_check_x2:
                        char = thresh_img[check_y:y+h, min(check_x,x):max(check_x+check_w,x+w)]
                        cv2.imshow("of",char)
                        cv2.waitKey(0)
                        #print(f"{check_x >= dot_check_x1 and check_y < dot_check_x2}")
                #print(f"number:{i}    x: {check_x}   y: {check_y}")
                        
            #print(f"{index}   dot check Xs: {dot_check_x1} {dot_check_x2}   dot check Ys: {dot_check_y1} {dot_check_y2}\n")
            
            
            ###########################################################################
            """

            contour_image = cv2.cvtColor(contour_image,cv2.COLOR_GRAY2BGR)

            #char = cv2.cvtColor(char,cv2.COLOR_BGR2GRAY)

            self.characterContours.append([contour_image,(x,y)])
            self.characterImages.append([char,(x,y)])
        

    def ShowLetters(self):
        for i,(image,coorditanions) in enumerate(self.characterImages):
            print(f"{i}  {image.shape}       coordinates:{coorditanions}")
            cv2.imshow(f"{i}",image)
            cv2.waitKey(0)

    def SaveExtraction(self):
        save_path = os.path.join( self.path , "Images")
        os.makedirs(save_path,exist_ok=True)

        for i, img in enumerate(self.characterImages):
            image = img[0]
            try:
                cv2.imwrite(f"{save_path}\\{i}.jpg",image)
            except Exception as e:
                print(f"There was an error saving number {i} :::: {e}")

class RecognitionSquarer:
    
    def __init__(self, characterImages):
        self.characterImages = characterImages
        self.characterSquares = []
        self.biggestLength = 0
        self.Squarer()

    
    def Squarer(self):

        for img, coordinates in self.characterImages:
            height, width = img.shape[:2]

            if max(height,width) > self.biggestLength:
                self.biggestLength = max(height,width)
            
            #print(f"height: {height} width: {width}     Biggest So far: {self.biggestLength}")
            
        for img, coordinates in self.characterImages:
            height, width = img.shape[:2]

            img_edge = max(height,width)

            max_edge = img_edge                                    #self.biggestLength

            x1,x2 = int( (max_edge - height) / 2 ), int( (max_edge + height) / 2 )
            y1,y2 = int( (max_edge - width) / 2 ), int( (max_edge + width) / 2 )
            

            blank_image = np.zeros((max_edge,max_edge), dtype=np.uint8)

            blank_image[x1:x2,y1:y2] = img

            IMAGE = blank_image

            x, y = coordinates

            x_center = (x + width)//2
            y_center = (y + height)//2
            
            self.characterSquares.append([IMAGE,(x_center,y_center),(height,width)])

    def ShowImages(self):
        for image,coordinates in self.characterSquares:
            cv2.imshow("lala", image)
            cv2.waitKey(0)

class Squarer:

    def __init__(self, characterImages):
        self.characterImages = characterImages
        self.characterSquares = []
        self.biggestLength = 0
        self.Squarer()

    
    def Squarer(self):

        for img, coordinates,name in self.characterImages:
            height, width = img.shape[:2]

            if max(height,width) > self.biggestLength:
                self.biggestLength = max(height,width)
            
            #print(f"height: {height} width: {width}     Biggest So far: {self.biggestLength}")
            
        for img, coordinates, name in self.characterImages:
            height, width = img.shape[:2]

            img_edge = max(height,width)

            max_edge = img_edge                                    #self.biggestLength

            x1,x2 = int( (max_edge - height) / 2 ), int( (max_edge + height) / 2 )
            y1,y2 = int( (max_edge - width) / 2 ), int( (max_edge + width) / 2 )
            

            blank_image = np.zeros((max_edge,max_edge), dtype=np.uint8)

            blank_image[x1:x2,y1:y2] = img

            IMAGE = blank_image
            self.characterSquares.append([IMAGE,coordinates,name])

    def ShowImages(self):
        for image,coordinates,name in self.characterSquares:
            cv2.imshow("lala", image)
            cv2.waitKey(0)

class BitMap:
    def __init__(self, Image, bit_size):
        self.bit_size = bit_size
        self.image = Image
        self.bitmap = np.zeros(self.bit_size**2, dtype=np.uint8)

        size = self.image.shape[0]

        while(True):
            if size % self.bit_size != 0:
                size += 1
                continue
            break

        if size != self.image.shape[0]:
            self.newSize = size
            self.image = cv2.resize(self.image,(size,size))

        self.CreateBitMap()

    def getBitmap(self):
        return self.bitmap

    def CreateBitMap(self):

        step_size = int(self.image.shape[0]/self.bit_size)
        
        for row in range(0,self.bit_size):
            for column in range(0,self.bit_size):
                
                bitmap_index = row*self.bit_size + column
                white_counter = 0

                for x in range( column*step_size , (column+1) * step_size ):
                    for y in range( row*step_size , (row+1) * step_size ):
                        if self.image[y,x] == 255:
                            white_counter += 1


                if (white_counter/(step_size**2)) >= 0.30:          #PERCENTAGE OF WHITE PIXELS
                    self.bitmap[bitmap_index] = 1
        
    def ShowBitMap(self):
        pass

    def PrintBitMap(self):
        for i in range(0,self.bit_size):
            print( self.bitmap[i * self.bit_size : (i+1) * self.bit_size ] )
        print("\n\n")

    def ShowImage(self):
        cv2.imshow("Image",self.image)
        cv2.waitKey(0)

class FontBitMaps:

    def __init__(self, images, StarterBit):

        self.characterImages = images

        self.bit = StarterBit
        self.CreateBitMaps()
        
    
    def CreateBitMaps(self):
        bit_maps = []

        for img,coordinates,name in self.characterImages:
            bitmap = BitMap(img,self.bit)
            bit_maps.append([bitmap,coordinates,name])
        
        self.bit_maps = bit_maps

        if not self.AreAllBitmapsUnique():
            self.bit += 1
            self.bit_maps = []
            print(f"Some BitMaps are repeated at bit size of {self.bit-1}.\nNew bit size: {self.bit}")
            self.CreateBitMaps()
    
    def AreAllBitmapsUnique(self, tolerance = 0):
        
        bitmap_amount = len(self.bit_maps)
        for i in range(bitmap_amount):
            for j in range(i+1, bitmap_amount):
                if np.array_equal(self.bit_maps[i][0].getBitmap(), self.bit_maps[j][0].getBitmap()):

                    if "line" in self.bit_maps[i][2] and "line" in self.bit_maps[j][2] or "dot" in self.bit_maps[i][2] and "dot" in self.bit_maps[j][2]:
                        continue

                    tolerance += 1
                    if tolerance >= 2:  return False
        return True
    
    def SaveBitmaps(self,save_path):
        json_data = {}
        for BITMAP in self.bit_maps:
            bit_map = BITMAP[0].bitmap
            name = BITMAP[2]

            json_data[name] = {
                "bitmap":bit_map.tolist()
            }
        with open(save_path,"w") as F:
            json.dump(json_data,F,indent=3)

class Symbol:
    def __init__(self,char,confidence,center,size):
        self.char = char
        self.confidence = confidence
        self.x, self.y = center
        self.w, self.h = size

    def isLine(self):
        return "line" in self.char
    
    def __repr__(self): 
        return f"Symbol('{self.char}', ({self.x}, {self.y}), {self.w}x{self.h})"


        