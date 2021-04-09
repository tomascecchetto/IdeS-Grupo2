package ids.androidsong.ui;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.app.Activity;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.cancionesDummy;
import ids.androidsong.help.App;
import ids.androidsong.help.Sincronizar;
import ids.androidsong.object.Cancion;
import ids.androidsong.object.DriveStatus;
import ids.androidsong.object.Item;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class cp5 {

    private static final int CANTIDAD_CANCIONES_DUMMY = 1;
    private static final int CANTIDAD_SECCIONES_DUMMY = 3;
    private static final String FALSO_HASH = "FALSO_HASH";
    private ArrayList<Cancion> canciones = new ArrayList<>();

    @Rule
    public ActivityTestRule<Principal> mActivityTestRule = new ActivityTestRule<>(Principal.class);

    @Before
    public void setup() {
        /*Pide canciones dummy y las inserta*/
        cancionesDummy cancionesDummy = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY);
        canciones.addAll(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY));
        for (Cancion c : canciones) {
            c.alta();
        }
    }

    /*@Test
    public void cp5_1() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sincronizar_log_sync), withText(
                        "\nCarpeta correctamente identificada.\nCanción Dummy 0, Subida a "
                                + "Drive\nSincronización completa."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(
                                                android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText(
                " Carpeta correctamente identificada. Canción Dummy 0, Subida a Drive "
                        + "Sincronización completa.")));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sincronizar_log_sync), withText(
                        "\nCarpeta correctamente identificada.\nCanción Dummy 0, Agregada a Android Song\nCanción Dummy 0, Eliminada de Android Song\nSincronización completa."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(
                                                android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText(
                " Carpeta correctamente identificada. Canción Dummy 0, Agregada a Android Song Canción Dummy 0, Eliminada de Android Song Sincronización completa.")));

    } //Bajar de Drive*/

    @Test
    public void cp5_2() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton.perform(click());

        DriveStatus status = new DriveStatus(canciones.get(0).getTitulo(),canciones.get(0).getCarpeta());
        status.fill();
        status.setDriveDT(null); //De este modo la sincronización pienza que la Cancion fue recién creada localmente
        status.modificacion();

        floatingActionButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView2.check(matches(withPartialText(
                "Canción Dummy 0, Conflicto detectado")));

    } //Conflicto dos nuevos - OK

    @Test
    public void cp5_3() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView.check(matches(withPartialText(
                "Canción Dummy 0, Subida a Drive")));

        canciones.get(0).baja();
        canciones.get(0).eliminar();

        floatingActionButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView2.check(matches(withPartialText(
                "Canción Dummy 0, Borrada en Drive")));

    } //Borrado localmente - OK

    @Test
    public void cp5_4() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView.check(matches(withPartialText(
                "Canción Dummy 0, Subida a Drive")));

        DriveStatus status = new DriveStatus(canciones.get(0).getTitulo(),canciones.get(0).getCarpeta());
        status.setDriveDT(FALSO_HASH); //De este modo la sincronización pienza que la versión de Drive cambió.
        status.modificacion();
        canciones.get(0).modificarAtributos();

        floatingActionButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView2.check(matches(withPartialText(
                "Canción Dummy 0, Conflicto detectado")));

    } //Conflicto dos modificados - OK

    @Test
    public void cp5_5() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView.check(matches(withPartialText(
                "Canción Dummy 0, Subida a Drive")));

        DriveStatus status = new DriveStatus(canciones.get(0).getTitulo(),canciones.get(0).getCarpeta());
        status.setDriveDT(FALSO_HASH);
        status.modificacion();

        floatingActionButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView2.check(matches(withPartialText(
                "Canción Dummy 0, Versión Local actualizada")));

    } //Actualizar desde Drive - OK

    @Test
    public void cp5_6() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView.check(matches(withPartialText(
                "Canción Dummy 0, Subida a Drive")));

        canciones.get(0).modificarAtributos();

        floatingActionButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView2.check(matches(withPartialText(
                "Canción Dummy 0, Versión de Drive actualizada")));

    } //Actualizar desde local - OK

    @Test
    public void cp5_7() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sincronizar_log_sync)));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView2.check(matches(withoutPartialText(
                "Canción Dummy 0")));

    } //Sin cambios - OK

    @Test
    public void cp5_8() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sincronizar_log_sync)));
        textView.check(matches(withPartialText(
                "Canción Dummy 0, Subida a Drive")));
    } //Subir a Drive - OK

    /*@Test
    public void cp5_9() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(
                                                        is("android.support.design.widget"
                                                                + ".AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sincronizar_log_sync), withText(
                        "\nCarpeta correctamente identificada.\nCanción Dummy 0, Subida a "
                                + "Drive\nSincronización completa."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(
                                                android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText(
                " Carpeta correctamente identificada. Canción Dummy 0, Subida a Drive "
                        + "Sincronización completa.")));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sincronizar_log_sync), withText(
                        "\nCarpeta correctamente identificada.\nCanción Dummy 0, Agregada a Android Song\nCanción Dummy 0, Eliminada de Android Song\nSincronización completa."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(
                                                android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText(
                " Carpeta correctamente identificada. Canción Dummy 0, Agregada a Android Song Canción Dummy 0, Eliminada de Android Song Sincronización completa.")));

    } //Borrado en Drive*/

    @After
    public void destroy(){
        try {
            ArrayList<Item> cancionesRemanentes = new Cancion().get();
            for (Item i: cancionesRemanentes){
                i.baja();
                i.eliminar();
            }
            Sincronizar sincBl = new Sincronizar((Activity)App.GetContext());
            sincBl.getDriveConnection();
            sincBl.sincronizarEnBackground(null);
            App.GetDBHelper().clearDb();
            App.CloseDB();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
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

    public class PartialTextMatcher extends TypeSafeMatcher<View> {

        private final String expectedText;

        public PartialTextMatcher(String expectedText) {
            super(View.class);
            this.expectedText = expectedText;
        }

        @Override
        protected boolean matchesSafely(View target) {
            if (!(target instanceof TextView)) {
                return false;
            }
            TextView targetEditText = (TextView) target;
            return targetEditText.getText().toString().contains(expectedText);
        }


        @Override
        public void describeTo(Description description) {
            description.appendText("with partialText: ");
            description.appendValue(expectedText);
        }
    }

    public Matcher<View> withPartialText(final String expectedText) {
        return new cp5.PartialTextMatcher(expectedText);
    }

    public class NoPartialTextMatcher extends TypeSafeMatcher<View> {

        private final String searchedText;

        public NoPartialTextMatcher(String searchedText) {
            super(View.class);
            this.searchedText = searchedText;
        }

        @Override
        protected boolean matchesSafely(View target) {
            if (!(target instanceof TextView)) {
                return false;
            }
            TextView targetEditText = (TextView) target;
            return !targetEditText.getText().toString().contains(searchedText);
        }


        @Override
        public void describeTo(Description description) {
            description.appendText("without partialText: ");
            description.appendValue(searchedText);
        }
    }

    public Matcher<View> withoutPartialText(final String searchedText) {
        return new cp5.NoPartialTextMatcher(searchedText);
    }
}
