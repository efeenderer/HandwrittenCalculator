import os
import cv2
import numpy as np

letter_dict = {
    '0001': 'a',
    '0002': 'b',
    '0003': 'c',
    '0004': 'd',
    '0005': 'e',
    '0006': 'f',
    '0007': 'h',
    '0008': 'line',
    '0009': 'j',
    '0010': 'k',
    '0011': 'line',
    '0012': 'm',
    '0013': 'n',
    '0014': 'o',
    '0015': 'p',
    '0016': 'q',
    '0017': 'r',
    '0018': 's',
    '0019': 't',
    '0020': 'u',
    '0021': 'v',
    '0022': 'w',
    '0023': 'x',
    '0024': 'y',
    '0025': 'z',
    '0026': '0',
    '0027': '1',
    '0028': '2',
    '0029': '3',
    '0030': '4',
    '0031': '5',
    '0032': '6',
    '0033': '7',
    '0034': '8',
    '0035': '9',
    '0036': 'plus',
    '0037': 'horizontal_line',
    '0038': 'slash',
    '0039': 'paranthesis_left',
    '0040': 'paranthesis_right',
    '0041': 'sqrt',
    '0042': 'sqrt'
}

dataset_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\dataset"

def ExtractSet(path, save_path, name = None):


    
    page = cv2.imread(path, cv2.IMREAD_GRAYSCALE)

    kernel = np.ones((4,4), np.uint8)

    blur = cv2.GaussianBlur(page,(3,3), 1)


    _, thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)

    dilation = cv2.dilate(thresh, kernel, iterations=2)

    contours, _ = cv2.findContours(dilation, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    
    for index, contour in enumerate(contours):
        x, y, w, h = cv2.boundingRect(contour)

        max_edge = max(h,w)
        
        blank = np.zeros((max_edge,max_edge),np.uint8)

        x1,x2 = int( (max_edge - h) / 2 ), int( (max_edge + h) / 2 )
        y1,y2 = int( (max_edge - w) / 2 ), int( (max_edge + w) / 2 )

        blank[x1:x2,y1:y2] = dilation[y:y+h, x:x+w]
        letter = cv2.resize(blank, (64,64))

        cv2.imwrite(save_path+f"\{index}.jpg", letter)
            

for dSet_folder in os.listdir(dataset_path):
    if len( dSet_folder.split(".") ) > 1:
        continue
    dSet_path = os.path.join(dataset_path,dSet_folder)

    for index, letter_page in enumerate( os.listdir(dSet_path) ):
        if letter_page.split(".")[-1] != "jpg":      continue

        letter_page_path = os.path.join(dSet_path, letter_page)
        letter_number = letter_page.split(".")[0].split("-")[-1]
        letter_name = letter_dict[letter_number]

        if "paranthesis" not in letter_name: continue
        
        print(f"{letter_name}        {letter_number}")

        save_path = os.path.join( dSet_path, letter_name )
        os.makedirs(save_path,exist_ok=True)


        ExtractSet(letter_page_path,save_path)

        







