from typing import List
import torch.nn as nn


class Symbol:
    def __init__(self,char,confidence=None,center=None,size=None):
        self.char = char
        self.confidence = confidence
        self.x, self.y = center
        self.w, self.h = size
        self.used = False

    def isLine(self):
        return "line" in self.char
    
    def __repr__(self): 
        return f"Symbol('{self.char}', ({self.x}, {self.y}), {self.w}x{self.h})"
    
    def __eq__(self, other):
        if not isinstance(other, Symbol):
            return False
        return (self.char, self.x, self.y, self.w, self.h) == (other.char, other.x, other.y, other.w, other.h)

    def __hash__(self):
        # Eğer __eq__'yi override ediyorsan, __hash__'i de buna uygun tanımlaman gerekir.
        return hash((self.char, self.x, self.y, self.w, self.h))

    def __repr__(self):
        return f"Symbol('{self.char}', ({self.x}, {self.y}), {self.w}x{self.h})"

uppercase_bit_size = 6
lowercase_bit_size = 6  #Based on trials
numbers_bit_size = 6
lower_operator_number_bit_size = 6

char_names = {}

def SortByX(symbols: List[Symbol]):

    return sorted(symbols, key=lambda s: s.x)

def ProcessLine(symbols: List[Symbol]):
    symbols = SortByX(symbols)

    result = ""

    for s in symbols:
        result += f"{s.char} "

    return result

def ProcessDivision(div_symbol, above, below, tolerance = 50):
    above_line = ProcessLine(above)
    below_line = ProcessLine(below)

    result = f"({above_line}) / ({below_line})"

    return Symbol(result, confidence=100.0,center=(div_symbol.x, div_symbol.y), size=(div_symbol.w, div_symbol.h))
    
def Process(symbols, tolerance = 50):

    symbols_x_sorted = SortByX(symbols)
    new_symbols = []
    i = 0
    used = set()

    while i < len(symbols_x_sorted):
        s = symbols_x_sorted[i]

        if "horizontal_line" in s.char:
            div_left = s.x - s.w//2 - tolerance
            div_right = s.x + s.w//2 + tolerance
            
            above = []
            below = []

            for s2 in symbols: 
                if s2 is s:
                    continue
                isItIn = div_left <= s2.x <= div_right
                if not isItIn:
                    continue
                if s2.y < s.y:
                    above.append(s2)
                elif s2.y > s.y:
                    below.append(s2)
            
            used = set(above+below +[s])

            new_symbol = ProcessDivision(s,above,below)
            new_symbols.append(new_symbol)

            symbols_x_sorted = [s3 for s3 in symbols_x_sorted if s3 not in used]
            i = 0

        else:
            i += 1

    for s in symbols_x_sorted:
        print(f"Deneme: {s}")

    return ProcessLine(new_symbols+symbols_x_sorted)


symbols = []



