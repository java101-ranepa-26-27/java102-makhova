package ru.ranepa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Тесты практики №2. Этот же файл лежит у студентов в src/test/java/ru/ranepa/ —
 * он задаёт имена классов и методов, к которым они подгоняют решение.
 *
 * Формат отчёта свободный: вывод проверяется по смыслу (имя, статус), а точные
 * сверки стоят там, где живут ловушки — средний балл и статус.
 */
class StudentTest {

    private static Student student(String name, int... grades) {
        Student s = new Student(name);
        for (int grade : grades) {
            s.addGrade(grade);
        }
        return s;
    }

    // --- средний балл ---

    @Test
    void среднийДробный_аНеЦелоеЧисло() {
        // 14/3 = 4.67; при целочисленном делении было бы 4
        assertEquals(14.0 / 3, student("A", 5, 4, 5).average(), 1e-9);
    }

    @Test
    void среднийРовный() {
        assertEquals(4.0, student("A", 4, 4, 4).average(), 1e-9);
    }

    @Test
    void однаОценка() {
        assertEquals(5.0, student("A", 5).average(), 1e-9);
    }

    @Test
    void нетОценок_среднийНоль() {
        // не 0/0 и не исключение
        assertEquals(0.0, student("A").average(), 1e-9);
    }

    // --- статус: пороги включительные ---

    @Test
    void ровноЧетыреСТочекПять_отличник() {
        assertEquals("отличник", student("A", 4, 5).status());
    }

    @Test
    void ровноТриСТочекПять_хорошист() {
        assertEquals("хорошист", student("A", 3, 4).status());
    }

    @Test
    void ровноДваСТочекПять_троечник() {
        assertEquals("троечник", student("A", 2, 3).status());
    }

    @Test
    void нетОценок_двоечник() {
        assertEquals("двоечник", student("A").status());
    }

    @Test
    void статусЛомаетсяОтЦелочисленногоДеления() {
        // 14/3 = 4.67 → отличник; при int-делении 4 → был бы хорошист
        assertEquals("отличник", student("A", 5, 4, 5).status());
    }

    @Test
    void статусНаГраницеЛомаетсяОтЦелочисленногоДеления() {
        // 7/2 = 3.5 → хорошист; при int-делении 3 → был бы троечник
        assertEquals("хорошист", student("A", 3, 4).status());
    }

    // --- отчёт: формат свободный, проверяем смысл ---

    @Test
    void отчетСодержитИмя() {
        assertTrue(student("Иванов Иван", 5, 4, 5).report().contains("Иванов Иван"));
    }

    @Test
    void отчетСодержитСтатус() {
        assertTrue(student("Иванов Иван", 5, 4, 5).report().contains("отличник"));
    }

    @Test
    void печатьОтчетаВыводитТотЖеТекст() {
        Student s = student("Петров Пётр", 3, 4);
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            s.printReport();
        } finally {
            System.setOut(original);
        }
        String printed = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Петров Пётр"), printed);
        assertTrue(printed.contains("хорошист"), printed);
    }

    // --- ввод целиком ---

    @Test
    void нольЗавершаетВводИНеСтановитсяОценкой() throws Exception {
        // если ноль попадёт в оценки, средний станет 14/4 = 3.5 и статус будет «хорошист»
        String printed = runMainWithInput("Иванов Иван\n5\n4\n5\n0\n");
        assertTrue(printed.contains("Иванов Иван"), printed);
        assertTrue(printed.contains("отличник"), printed);
    }

    @Test
    void сразуНоль_безДеленияНаНоль() throws Exception {
        String printed = runMainWithInput("Без Оценок\n0\n");
        assertTrue(printed.contains("Без Оценок"), printed);
        assertTrue(printed.contains("двоечник"), printed);
        assertFalse(printed.contains("Infinity"), printed);
        assertFalse(printed.contains("NaN"), printed);
    }

    private static String runMainWithInput(String input) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Main.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
