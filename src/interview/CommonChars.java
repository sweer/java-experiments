package interview;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import sort.QuicksortParallel;

public class CommonChars {
    public static void main(String[] args) {
        System.out.println(commonChars("abduusl", "budit"));

    }

    private static String commonChars(String a, String b) {
        Set<Character> sa = a.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
        Set<Character> result = b.chars().mapToObj(c -> (char) c).filter(sa::contains).collect(Collectors.toSet());
        return result.stream().map(Object::toString).collect(Collectors.joining());
    }


}
