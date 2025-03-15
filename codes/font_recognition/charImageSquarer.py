import os
import numpy as np
import cv2 
import time as t

font_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\fonts\arial"           #first tests with aerial
save_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\letters_square\arial"
LOG_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\charImageSquarer_errorLOG.txt"

TEXT = ""
for image in os.listdir(font_path):
    try:
        image_path = os.path.join(font_path,image)
        image_type = "No Type"
        image_name = image.split(".")[-2]

        if "_" in image_name:
            image_type = image_name.split("_")[-2]

        print(f"Image name: {image_name}    type: {image_type}\n")


        if image_type == "capital" or image_type == "lower" or image_type =="sign" or image_type =="number":
            letter = image_name.split("_")[-1]
            print(f"Letter: {letter}\n")
            
            Image = cv2.imread(image_path,cv2.IMREAD_GRAYSCALE)
            height = Image.shape[0]
            width = Image.shape[1]
            max_edge = max(height,width)
            
            starting_x =int( (max_edge - width)/2)
            ending_x = int( (max_edge + width)/2) 

            starting_y = int((max_edge - height)/2)
            ending_y = int((max_edge + height)/2) 

            print(f"Width of the Image: {width}\nHeight of the Image: {height}\nMax edge: {max_edge}\nStarting_x: {starting_x} Ending_x: {ending_x}\nStarting_y: {starting_y} Ending_y: {ending_y}\n")

            blank_square = np.zeros((max_edge,max_edge), dtype=np.uint8)
            

            blank_square[starting_y:ending_y , starting_x:ending_x] = Image

            #cv2.imshow("blank",blank_square)
            cv2.imwrite(os.path.join(save_path, image_name+"_square.png"), blank_square )
            cv2.waitKey(0)
        print("\n\n")

    except Exception as e:
        TEXT += f"you tell me what's wrong:: {e}   \n\n  error causing image: {image}\n"
    TEXT += "\n\n\n"

with open(LOG_path,"w") as f:
    f.write(TEXT)

