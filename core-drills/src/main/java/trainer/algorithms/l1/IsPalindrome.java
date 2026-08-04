package trainer.algorithms.l1;

import java.util.Objects;

// @task algorithms.l1.IsPalindrome
// @tags strings,two-pointers,palindrome
// @time 8m
// @src  new
public final class IsPalindrome {

    private IsPalindrome() {
    }

    /** Игнорирует регистр и символы, которые не являются буквами или цифрами. */
    public static boolean test(String value) {
        Objects.requireNonNull(value, "value");

        // ---8<--- solution
        int left = 0;
        int right = value.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(value.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(value.charAt(right))) {
                right--;
            }
            if (Character.toLowerCase(value.charAt(left)) != Character.toLowerCase(value.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
        // --->8--- solution
    }
}
