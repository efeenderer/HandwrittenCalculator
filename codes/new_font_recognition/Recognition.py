from typing import List
from Recognition_Classes import *

uppercase_bit_size = 6
lowercase_bit_size = 6  #Based on trials
numbers_bit_size = 6
lower_operator_number_bit_size = 6


r"""
uppercase_recognition_font_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\uppercase\bitmaps\arial_uppercase.json"
lowercase_recognition_font_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lowercase\bitmaps\el_yazisi_lowercase.json"
numbers_recognition_font_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\numbers\bitmaps\arial_numbers.json"


uppercase_recognition_test_image_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\TEST\uppercase\uppercase_test.jpg"
lowercase_recognition_test_image_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\TEST\lower_operator_number\el_yazisi_test_1.jpg"
numbers_recognition_test_image_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\TEST\numbers\numbers_test.jpg"


uppercase_extraction = Extractor(uppercase_recognition_test_image_path)
uppercase_squarer = RecognitionSquarer(uppercase_extraction.characterImages)

lowercase_extraction = Extractor(lowercase_recognition_test_image_path)
lowercase_squarer = RecognitionSquarer(lowercase_extraction.characterImages)

numbers_extraction = Extractor(numbers_recognition_test_image_path)
numbers_squarer = RecognitionSquarer(numbers_extraction.characterImages)

"""


def ManhattanLike(image_bitmap_object, font_bitmap_path):

    with open(font_bitmap_path,"r") as F:
        data = json.load(F)
    image_bitmap = image_bitmap_object.bitmap
    results = []

    softmax_total = 0
    
    best = ["None",0]

    for font_letter_data in data:

        font_letter_bitmap = np.array(data[font_letter_data]["bitmap"])
        #print(f"type of image_bitmap: {(image_bitmap)}    type fof font: {(font_letter_bitmap)}")
        difference = np.where(font_letter_bitmap == image_bitmap,1,0)
        result = np.sum(difference)
        results.append([font_letter_data,result])

        softmax_total += np.exp(result)

    for result in results:
        softmax = np.exp(result[1])/softmax_total
        percentage = np.clip(softmax*100,1e-5,100- 1e-5)
        #print(f"Letter {result[0]} - Percentage: {percentage}")
        if best[1] < percentage:
            best = [result[0],percentage]

    return best

def sort_symbols_left_to_right(symbols):
    return sorted(symbols, key=lambda s: s[1][0])  # sort by x_center

def is_superscript(base, candidate):
    # candidate üstte ve küçükse üstsimgedir
    _, (bx, by), (bw, bh) = base
    _, (cx, cy), (cw, ch) = candidate
    return (cy + ch) < by and ch < bh * 0.8 and abs(cx - bx) < bw * 1.2

def get_neighbors(line, symbols, direction='above'):
    lx, ly = line[1]
    lw, lh = line[2]
    xmin, xmax = lx - lw * 0.1, lx + lw * 1.1
    results = []
    for sym in symbols:
        if sym == line:
            continue
        cx, cy = sym[1]
        cw, ch = sym[2]
        cx_center = cx
        in_x_range = xmin <= cx_center <= xmax
        if direction == 'above':
            if in_x_range and cy + ch < ly and abs(cy + ch - ly) < lh * 4:
                results.append(sym)
        else:
            if in_x_range and cy > ly + lh and abs(cy - (ly + lh)) < lh * 4:
                results.append(sym)
    return sorted(results, key=lambda s: s[1][0])  # sort by x_center

def build_expression(symbols):
    expr = ""
    i = 0
    symbols = sort_symbols_left_to_right(symbols)
    
    while i < len(symbols):
        current = symbols[i]
        char_label = current[0][0]

        if 'h_line' in char_label:
            above = get_neighbors(current, symbols, 'above')
            below = get_neighbors(current, symbols, 'below')
            
            if above and below:
                above_expr = build_expression(above)
                below_expr = build_expression(below)
                expr += f"({above_expr})/({below_expr})"
                # Bu sembolleri atlıyoruz:
                skip = set([tuple(s) for s in above + below + [current]])
                symbols = [s for s in symbols if tuple(s) not in skip]
                i = 0
                continue
            else:
                expr += "-"  # yalnız bir çizgiyse eksi olarak algıla

        else:
            # Superscript kontrolü
            superscripts = [s for s in symbols if is_superscript(current, s)]
            if superscripts:
                # En yakın üstü al
                superscript_expr = build_expression(superscripts)
                expr += f"{char_label}^({superscript_expr})"
                skip = set([tuple(s) for s in superscripts])
                symbols = [s for s in symbols if tuple(s) not in skip]
                i += 1
                continue
            else:
                expr += char_label
        
        i += 1

    return expr




def ToString(uppercase_letters_list,tolerance = 10):
    y_values = set()
    groups = []

    for _,coordinates in uppercase_letters_list:
        y_values.add(coordinates[1])

    y_values = sorted(y_values)


    temp_value = y_values[0]

    simplified_y_values = [temp_value]

    for i in range(1,len(y_values)):
        if y_values[i] - temp_value <= tolerance:
            continue
        else:
            temp_value = y_values[i]
            simplified_y_values.append(temp_value)
    
    letters_by_y_values = {key: [] for key in simplified_y_values}

    
    for letter in uppercase_letters_list:
        coordinates = letter[1]
        
        for y in simplified_y_values:
            if abs(coordinates[1] - y) <= tolerance:
                letters_by_y_values[y].append(letter)
    
    for letter_list in letters_by_y_values:
        letters_by_y_values[letter_list] = sorted(letters_by_y_values[letter_list], key=lambda x: x[1][0])

    for letter_list in letters_by_y_values:
        for [letter,percentage],_ in letters_by_y_values[letter_list]:
            print(letter,end=" ")
        print()

def ToStringLowercase(uppercase_letters_list,tolerance = 15):
    y_values = set()
    groups = []

    for _,coordinates in uppercase_letters_list:
        y_values.add(coordinates[1])

    y_values = sorted(y_values)


    temp_value = y_values[0]

    simplified_y_values = [temp_value]

    for i in range(1,len(y_values)):
        if y_values[i] - temp_value <= tolerance:
            continue
        else:
            temp_value = y_values[i]
            simplified_y_values.append(temp_value)
    
    letters_by_y_values = {key: [] for key in simplified_y_values}

    
    for letter in uppercase_letters_list:
        coordinates = letter[1]
        
        for y in simplified_y_values:
            if abs(coordinates[1] - y) <= tolerance and "dot" not in letter[0]:
                letters_by_y_values[y].append(letter)
    
    for letter_list in letters_by_y_values:
        letters_by_y_values[letter_list] = sorted(letters_by_y_values[letter_list], key=lambda x: x[1][0])

    for letter_list in letters_by_y_values:
        for [letter,percentage],_ in letters_by_y_values[letter_list]:
            print(letter,end=" ")
        print()



lower_operator_number_test_image_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\TEST\lower_operator_number\math_test_1.jpg"

lower_operator_number_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lower_operator_number\bitmaps\arial_lower_operator_number.json"

lower_operator_number_extraction = Extractor(lower_operator_number_test_image_path)
lower_operator_number_squarer = RecognitionSquarer(lower_operator_number_extraction.characterImages)


uppercase_letters_list = []
lowercase_letters_list = []
numbers_letters_list = []
lower_operator_number_list = []

for lower_operator_number_image, lower_operator_number_coordinates, lower_operator_number_sizes in lower_operator_number_squarer.characterSquares:
    
    bitmap_object = BitMap(lower_operator_number_image,lower_operator_number_bit_size)
    recognition = ManhattanLike(bitmap_object, lower_operator_number_bitmap_path)
    lower_operator_number_list.append([recognition, lower_operator_number_coordinates])

    
    print(f"{[recognition, lower_operator_number_coordinates, lower_operator_number_sizes]}")
    cv2.namedWindow("lala", cv2.WINDOW_FREERATIO)
    cv2.imshow("lala",lower_operator_number_image)
    cv2.waitKey(0)






r"""
for lowercase_letter_image, lowercase_letter_coordinates in lowercase_squarer.characterSquares:

    
    bitmap_object = BitMap(lowercase_letter_image,lowercase_bit_size)

    recognition = ManhattanLike(bitmap_object, lowercase_recognition_font_bitmap_path)
    lowercase_letters_list.append( [ recognition , lowercase_letter_coordinates ] )
    
    print([recognition, lowercase_letter_coordinates])
    cv2.imshow("ll", lowercase_letter_image)
    cv2.waitKey(1000)

for uppercase_letter_image, uppercase_letter_coordinates in uppercase_squarer.characterSquares:

    
    bitmap_object = BitMap(uppercase_letter_image,uppercase_bit_size)
    recognition = ManhattanLike(bitmap_object, uppercase_recognition_font_bitmap_path)
    uppercase_letters_list.append( [ recognition , uppercase_letter_coordinates ] )
    

for lowercase_letter_image, lowercase_letter_coordinates in lowercase_squarer.characterSquares:

    
    bitmap_object = BitMap(lowercase_letter_image,lowercase_bit_size)

    recognition = ManhattanLike(bitmap_object, lowercase_recognition_font_bitmap_path)
    lowercase_letters_list.append( [ recognition , lowercase_letter_coordinates ] )

for numbers_letter_image, numbers_letter_coordinates in numbers_squarer.characterSquares:

    
    bitmap_object = BitMap(numbers_letter_image,numbers_bit_size)

    recognition = ManhattanLike(bitmap_object, numbers_recognition_font_bitmap_path)
    numbers_letters_list.append( [ recognition , numbers_letter_coordinates ] )


print("\n\nUPPERCASE TESTS\n")
ToString(uppercase_letters_list)

print("\n\nLOWERCASE TESTS\n")
ToStringLowercase(lowercase_letters_list)


print("\nNUMBERS TESTS\n")
ToString(numbers_letters_list)"""