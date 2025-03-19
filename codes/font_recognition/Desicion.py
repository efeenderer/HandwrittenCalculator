import os
import numpy as np
import cv2   #might not be needed


bitmaps_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\bitmaps"
desicions_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\desicions"



class Node:
    def __init__(self, char_name, bitmap, rightChild,leftChild):
        self.char_name = char_name
        self.bitmap = bitmap
        self.rightChild = rightChild
        self.leftChild = leftChild

class Tree:
    def __init__(self,contents):
        self.contents = contents
        

    def MostImportantFeature(self):    
        
        feature_amounts = np.zeros(25,np.uint8).tolist()

        for content in contents:
            #img_name = content.split("#")[0]

            bitmap = content.split("#")[1]
            bitmap = bitmap[1:-1]
            bitmap = bitmap.split(", ")

            for index, bit in enumerate(bitmap):
                if bit == "0":
                        feature_amounts[index] += 1
        print(feature_amounts)

        most_important = 0
        difference = 70
        
        for index, amount in enumerate(feature_amounts):
            if abs(amount - 35) < difference:
                most_important = index
                difference = abs(amount - 35)

        return most_important

    def CreateTree(self):
        pass
        


for bitmaps in os.listdir(bitmaps_path):
    font_name = bitmaps.split(".")[0]

    text = f"{font_name} "
    path = os.path.join(bitmaps_path,bitmaps)

    

    with open(path,"r") as f:
        contents = f.read().split("\n")

        newTree = Tree(contents)
        newTree.CreateTree()

        print(text)




