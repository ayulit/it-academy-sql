1. Считываем строки в цикле

2. Если строка не пустая валидируем на корректность матрицы
    * Заглавная буква в начале в соответствии с английским алфавитом
    * Повторятся буквы не могут
    * открывающая / закрывающая скобка
    * количество элементов в каждом ряде матрицы одно и то же
    * ряды отделены точкой с запятой
    * элементы 
        * отделены пробелами 
        * целые числа
    * правильный размер матрицы для операций

3. Если строка пустая далее идет строка с операциями, валидируем
    * состоит из операндов и операций
    * операнды - должны совпадать с буквами считанных матриц
    * операторы только +/-/* 

4. Приоритеты операций
    * произведение *
    * сложение +
    * вычитание -

5. Вычисление
    * происходит слева направо в соответствии с приоритетами (4)
    * результат представлен в виде матрицы по правилам (2) без буквы и =