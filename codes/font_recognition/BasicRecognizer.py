import os
import numpy as np
import cv2 
import time as t

bits = 7
percentage = .50


class FeatureNode:
    
    def __init__(self, feature_index, left_child, right_child):
        self.index = feature_index
        self.right_child = right_child
        self.left_child = left_child

class BitmapNode:
    def __init__(self, name, Bitmaps):
        self.name = name
        self.bitmaps = Bitmaps 

class Tree: 
    
    def __init__(self, bitmap_path):
        self.bitmap_path = bitmap_path
        self.counter = 0
        with open(bitmap_path, "r") as f:
            self.contents = f.read().split("\n")

        self.bitmaps = self.ContentsToBitmap(self.contents)
        self.root = self.BuildTree(self.bitmaps)
                    
    def AreAllTheSame(self,bitmaps):
        if not bitmaps:
            return False
        
        #print(f"{bitmaps}")
        memory_bitmap = bitmaps[0][1]
        for bitmap in bitmaps[1:]:
            if bitmap[1] != memory_bitmap:
                return False
            
        return True
    
    def InorderTraversal(self, node=None):
        if node is None:
            node = self.root

        if isinstance(node, FeatureNode):
            self.InorderTraversal(node.left_child)
            print(f"Feature Index: {node.index}")
            self.InorderTraversal(node.right_child)
        elif isinstance(node, BitmapNode):
            print(f"Leaf Node: {node.name}")
        self.counter += 1

    def BuildTree(self, bitmaps):

        if not bitmaps:  # Eğer bitmaps tamamen boşsa
            print("Uyarı: Boş bir bitmap listesi geldi! Yaprak düğüm oluşturuluyor.")
            return BitmapNode("Unknown", None)  # İsimsiz bir yaprak düğüm

        if self.AreAllTheSame(bitmaps):
            name, bitmap = bitmaps[0]
            return BitmapNode(name,bitmap)

        root_Important_index = self.FindMostImportantIndex(bitmaps)
        #print(f"Most Important Index: {root_Important_index}")
        #print(f"{bitmaps}")
        left_bitmaps, right_bitmaps = self.SplitInto(bitmaps, root_Important_index)

        left_child = self.BuildTree(left_bitmaps)
        right_child = self.BuildTree(right_bitmaps)

        return FeatureNode(root_Important_index,left_child,right_child)
    
    def ContentsToBitmap(self, contents):
    
        bit_maps = []
        
        for content in contents:

                bitmap = content.split("#")[1]
                name = content.split("#")[0]

                bitmap = bitmap[1:-1]
                bitmap = bitmap.split(", ")

                bit_maps.append([name,bitmap])
        #print(f"bit maps: {bit_maps}")
        return bit_maps

    def FindMostImportantIndex(self, bit_maps: list[str]):

        feature_amounts = np.zeros(bits**2,np.uint8).tolist()

        orta = (len(bit_maps))/2

        for bitmap in bit_maps:
            bitmap = bitmap[1]

            for index, bit in enumerate(bitmap):
                if bit == "0":
                    feature_amounts[index] += 1

        most_important = 0
        difference = 70
        print(f"Feature Amounts: {feature_amounts}")

        for index, amount in enumerate(feature_amounts):
            if abs(amount - orta) < difference:
                most_important = index
                difference = abs(amount - orta)
        
        return most_important

    def SplitInto(self, bit_maps, index):
        bitmaps_white = []
        bitmaps_black = []

        for bitmap in bit_maps:
            if bitmap[1][index] == "0":
                bitmaps_black.append(bitmap)
            elif bitmap[1][index] == "255":
                bitmaps_white.append(bitmap)

        return bitmaps_white, bitmaps_black
            
    def Desicion(self, bitmap):

        node = self.root

        while isinstance(node,FeatureNode):
            #print(f"Node Index: {type(bitmap[node.index])}")
            if str(bitmap[node.index]) == "255":
                node = node.left_child
            else:
                node = node.right_child

        return node.name
    
    def __repr__(self):
        return f"The Bitmap Tree of {self.bitmap_path}"

class LetterExtractor:
    def __init__(self, text_image):
        self.img = text_image
        self.Letters = []
        pass

    def ContourExtraction(self):
        
        gray = cv2.cvtColor(self.img,cv2.COLOR_BGR2GRAY)
        height, width = gray.shape[:2]

        print(f"Height: {height}   Width: {width}")

        _,thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU) 
        
        contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)

        for contour in contours:
            for point in contour:
                x, y = point[0]

            x, y, w, h = cv2.boundingRect(contour)
            
            if w<10 or h<10: #If it's this small, it probably is just a noise
                continue

            img = thresh[y:y+h, x:x+w]

            self.Letters.append([img, (x,y)])

    def ShowImages(self):

        for img in self.Letters:
            Image = img[0]

class LetterRecognizer:
    
    def __init__(self, Letters: list):
        self.Letters = Letters
        self.LettersSquared = []
        self.ImageSquarer()
        self.BitMapExtraction()

    def ImageSquarer(self):

        for img_and_position in self.Letters:
            img = img_and_position[0]
            x,y = img_and_position[1]

            height = img.shape[0]
            width = img.shape[1]
            max_edge = max(height,width)

            starting_x = int( (max_edge - width)/2)
            ending_x = int( (max_edge + width)/2) 

            starting_y = int((max_edge - height)/2)
            ending_y = int((max_edge + height)/2) 
            
            blank_square = np.zeros((max_edge,max_edge), dtype=np.uint8)
            blank_square[starting_y:ending_y , starting_x:ending_x] = img
            
            blank_square = cv2.resize(blank_square, (100,100), cv2.INTER_NEAREST)
            
            self.LettersSquared.append([blank_square, (x,y)])

            #cv2.imshow("Image", blank_square)
            #cv2.waitKey(0)
            #cv2.destroyAllWindows()
            #print(f"{x} {y}")

    def BitMapExtraction(self):
        self.bit_vectors = []

        for Letter in self.LettersSquared:
            Image = Letter[0]
            x,y = Letter[1]

            bit_vector = np.zeros(bits**2,np.uint8)

            edge = Image.shape[0]

            step_size = int(edge/bits)

            for row in range(0,bits):
                for column in range(0,bits):

                    index = row*bits + column
                    white_counter = 0

                    for x in range(column*step_size , (column + 1) * step_size):
                        for y in range(row*step_size , (row + 1) * step_size):
                            if Image[x,y] == 255:
                                white_counter += 1
                    condition = white_counter/(step_size**2) >= percentage
                    if condition:
                        bit_vector[index] = 255

            self.bit_vectors.append([bit_vector, (x,y)])
        
    def ShowBitmap(self):

        for bit_vector in self.bit_vectors:
            bit_map = bit_vector[0]
            
            x,y = bit_vector[1]

            bit_map_2d = bit_map.reshape((bits, bits))
            scale_factor = 20 
            image_resized = cv2.resize(bit_map_2d, (bits * scale_factor, bits * scale_factor), interpolation=cv2.INTER_NEAREST)
            image_resized = np.transpose(image_resized)
            
            cv2.imshow("lala",image_resized)
            cv2.waitKey()
            print(f"{x} {y}")


def ContentsToBitmap(contents):

    bit_maps = []
    
    for content in contents:

            bitmap = content.split("#")[1]
            name = content.split("#")[0]

            bitmap = bitmap[1:-1]
            bitmap = bitmap.split(", ")

            bit_maps.append([name,bitmap])
    #print(f"bit maps: {bit_maps}")
    return bit_maps

Image = cv2.imread(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\FinalDesicion\test_text.jpg")

fonts = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\bitmaps"

extractor = LetterExtractor(Image)
extractor.ContourExtraction()

recognizer = LetterRecognizer(extractor.Letters)


def OneByOne(bit__map):
    arial = os.path.join(fonts,"arial.txt")

    with open(arial,"r") as f:
        contents = f.read().split("\n")
    
    bitmaps = ContentsToBitmap(contents)

    for bitmap in bitmaps:
        name = bitmap[0]
        bit_map = bitmap[1]
        counter = 0
        for index, bit in enumerate(bit_map):
            if str(bit__map[index]) == bit:
                counter += 1
        print(f"{name} Percentage: {(counter/(bits**2))*100}%")

"""
for index, bit_vector in enumerate(recognizer.bit_vectors):
    OneByOne(bit_vector[0])

    cv2.imshow("lala",recognizer.LettersSquared[index][0])
    cv2.waitKey(0)

quit(1)"""
agac = Tree(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\bitmaps\arial.txt")

for index, letter in enumerate(recognizer.bit_vectors):

    letter_bitmap = letter[0]
    letter_position = letter[1]
    letter_image = recognizer.LettersSquared[index][0]
    print(letter_bitmap)

    

    print(agac.Desicion(letter_bitmap))
    cv2.imshow("lala", letter_image)
    cv2.waitKey(0)

