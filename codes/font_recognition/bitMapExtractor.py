import numpy as np
import cv2 
import os

bits = 6
percentage = .25

def BitMap(Image: np.ndarray) -> np.ndarray:

    bit_vector = np.zeros(bits**2,np.uint8)


    edge = Image.shape[0]
    

    step_size = int(edge/bits)
    print(f"Edge: {edge} Bits: {bits} Step Size: {step_size}")

    for row in range(0,bits):
        for column in range(0,bits):
            
            index = row*bits + column
            white_counter = 0

            for x in range(column*step_size , (column + 1) * step_size):
                for y in range(row*step_size , (row + 1) * step_size):
                    if Image[x,y] == 255:
                        white_counter += 1

            #print(f"white amount on {row} {column} is : {white_counter}")
            
            condition = white_counter/(step_size**2) >= percentage
            if condition:
                bit_vector[index] = 255
    
    return bit_vector


characters_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\letters_square"
image_saves_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\bitmaps_images"
bitmap_saves_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\bitmaps"


text =""
for font in os.listdir(characters_path):
    print(f"{font}")
    try: 
        print("\n\n\n")
        font_path = os.path.join(characters_path,font)
        image_save_path = os.path.join(image_saves_path, str(bits), font)
        
        os.makedirs(image_save_path,exist_ok=True)
        
        for char in os.listdir(font_path):
            char_name = char.split(".")[-2].split("_")[-2]
            char_type = char.split(".")[-2].split("_")[-3]
            text += f"{char_type}_{char_name}#"

            img_path = os.path.join(font_path,char)
            Image = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)
            Image = cv2.resize(Image, (108,108),interpolation=cv2.INTER_NEAREST)

            bit_map = BitMap(Image)
            
            text += f"{bit_map.tolist()}\n"

            bit_map_2d = bit_map.reshape((bits, bits))

            scale_factor = 20  

            image_resized = cv2.resize(bit_map_2d, (bits * scale_factor, bits * scale_factor), interpolation=cv2.INTER_NEAREST)
            image_resized = np.transpose(image_resized)
            cv2.imwrite(os.path.join(image_save_path, char), image_resized)


            bitmap_save_path = os.path.join(bitmap_saves_path,font + ".txt")

        with open(bitmap_save_path,"w") as f:
            text = text[:-1]
            f.write(text) 
            text =""




    except Exception as e:
        print(f"Error: {e}")


