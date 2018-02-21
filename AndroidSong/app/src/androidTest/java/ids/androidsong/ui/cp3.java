package ids.androidsong.ui;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ids.androidsong.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class cp3 {

    @Rule
    public ActivityTestRule<principal> mActivityTestRule = new ActivityTestRule<>(principal.class);

    @Test
    public void cp3_1() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction seccion = onView(allOf(withId(R.id.seccion_contenido), withText(" C    F#      G\nContenido Dummy 1\n")));
        seccion.check(matches(withFontSize(16*2)));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.cancion_detalle_mayor)));
        appCompatImageButton2.perform(click());
        appCompatImageButton2.perform(click());
        appCompatImageButton2.perform(click());

        seccion.check(matches(withFontSize(22*2)));
    }

    @Test
    public void cp3_2() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction seccion = onView(allOf(withId(R.id.seccion_contenido), withText(" C    F#      G\nContenido Dummy 1\n")));
        seccion.check(matches(withFontSize(16*2)));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.cancion_detalle_menor)));
        appCompatImageButton2.perform(click());
        appCompatImageButton2.perform(click());
        appCompatImageButton2.perform(click());

        seccion.check(matches(withFontSize(10*2)));
    }

    @Test
    public void cp3_5() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.cancion_detalle_info),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.atributo_nombre), withText("autor"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lista_atributos),
                                        3),
                                0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.atributo_nombre), withText("himno_numero"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lista_atributos),
                                        4),
                                0),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.atributo_nombre), withText("interprete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lista_atributos),
                                        5),
                                0),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.atributo_nombre), withText("presentacion"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lista_atributos),
                                        6),
                                0),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.atributo_nombre), withText("tono"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lista_atributos),
                                        8),
                                0),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.atributo_nombre), withText("tono"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lista_atributos),
                                        8),
                                0),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));

    }

    @Test
    public void cp3_6() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.cancion_detalle_editar),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.editar_cancion_letra)));
        editText.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public class FontSizeMatcher extends TypeSafeMatcher<View> {

        private final int expectedSize;

        public FontSizeMatcher(int expectedSize) {
            super(View.class);
            this.expectedSize = expectedSize;
        }

        @Override
        protected boolean matchesSafely(View target) {
            if (!(target instanceof TextView)) {
                return false;
            }
            TextView targetEditText = (TextView) target;
            return targetEditText.getTextSize() == expectedSize;
        }


        @Override
        public void describeTo(Description description) {
            description.appendText("with fontSize: ");
            description.appendValue(expectedSize);
        }
    }

    public Matcher<View> withFontSize(final int fontSize) {
        return new FontSizeMatcher(fontSize);
    }
}
