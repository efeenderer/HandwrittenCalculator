from Recognition_Classes import *

uppercase_bit_size = 5
lowercase_bit_size = 8  #Based on trials
numbers_bit_size = 6

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
        difference = np.where(font_letter_bitmap == image_bitmap,1,-1)
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




uppercase_recognition_font_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\uppercase\bitmaps\arial_uppercase.json"
lowercase_recognition_font_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lowercase\bitmaps\arial_lowercase.json"
numbers_recognition_font_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\numbers\bitmaps\arial_numbers.json"

uppercase_recognition_test_image_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\TEST\uppercase\uppercase_test.jpg"
lowercase_recognition_test_image_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\TEST\lowercase\lowercase_test.jpg"
numbers_recognition_test_image_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\TEST\numbers\numbers_test.jpg"


uppercase_extraction = Extractor(uppercase_recognition_test_image_path)
uppercase_squarer = RecognitionSquarer(uppercase_extraction.characterImages)

lowercase_extraction = Extractor(lowercase_recognition_test_image_path)
lowercase_squarer = RecognitionSquarer(lowercase_extraction.characterImages)

numbers_extraction = Extractor(numbers_recognition_test_image_path)
numbers_squarer = RecognitionSquarer(numbers_extraction.characterImages)

uppercase_letters_list = []
lowercase_letters_list = []
numbers_letters_list = []

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

ToString(lowercase_letters_list)


print("\nNUMBERS TESTS\n")

ToString(numbers_letters_list)