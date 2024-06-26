package com.vaccine.Controller;

import com.vaccine.Utils.CalendarActivity;
import com.vaccine.Model.Entity.*;
import com.vaccine.Service.*;

import com.vaccine.Utils.Connection;
import com.vaccine.Utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.*;

// Random Code
public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;
    @FXML
    private Text month;
    @FXML
    private FlowPane calendar;

    private final PersonService personService;
    private final VaccineService vaccineService;
    private final Session session = Session.getInstance();
    private static final Logger log = LogManager.getLogger(LoginController.class);


    public CalendarController() {
        this.personService = PersonService.getInstance(Connection.getEntityManager());
        this.vaccineService = VaccineService.getInstance(Connection.getEntityManager());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now(); // Set focus date to current date
        today = ZonedDateTime.now(); // Set today's date
        drawCalendar(); // Draw the calendar
    }

    @FXML
    void backOneMonth() {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth() {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    public void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.GREEN);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }
    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    // On '...' click, print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName());
            Text vacText = new Text(calendarActivities.get(k).getVaccineName());
            calendarActivityBox.getChildren().add(text);
            calendarActivityBox.getChildren().add(vacText);
            text.setOnMouseClicked(mouseEvent -> {
                // On text clicked, print the text
                System.out.println(text.getText()+"\n"+vacText.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth((rectangleWidth * 0.8)-20);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:#B2A6CE");
        stackPane.getChildren().add(calendarActivityBox);
    }

    // Create a map of calendar activities for a month
    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> oldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(oldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }

    // Get calendar activities for a month
    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        Random random = new Random();

        for (PersonVaccine p : checkNotificatonList()) {
            ZonedDateTime time = ZonedDateTime.of(year, month, random.nextInt(27) + 1, 16, 0, 0, 0, dateFocus.getZone());
            log.info(p.getPerson().getName());
            CalendarActivity activity = getCalendarActivity(p, time);
            calendarActivities.add(activity);
        }
        return createCalendarMap(calendarActivities);
    }

    private static CalendarActivity getCalendarActivity(PersonVaccine p, ZonedDateTime time) {
        CalendarActivity activity =new CalendarActivity(time);
        String[] names = p.getPerson().getName().split("\\s+");
        String firstName = names[0];
        activity.setClientName(firstName);
        String[] vaccines = p.getVaccine().getVaccineName().split("\\s+");
        String vaccine = vaccines[0];
        if (!Objects.equals(vaccines[1], "Vaccine")){
            vaccine = vaccines[0]+" "+vaccines[1];
        }
        activity.setVaccineName(vaccine);
        return activity;
    }

    private List<PersonVaccine> checkNotificatonList() {
        List<Person> personList = personService.getPersonsList(session.getUser().getId());
        List<PersonVaccine> personVaccineToDo = new ArrayList<>();

        for (Person person : personList) {
            Period age = Period.between(Date.valueOf(String.valueOf(person.getDateOfBirth())).toLocalDate(), LocalDate.now());
            int personAge = age.getYears();
            List<Vaccine> unmadeVaccines = vaccineService.getUnassignedVaccinesForPerson(person.getId());
            if(!unmadeVaccines.isEmpty()){
                for (Vaccine vaccine : unmadeVaccines) {
                    if (vaccine.getAgeOfUse() <= personAge) {
                        PersonVaccine newPV = createNewPersonVaccine(personService.getById(person.getId()),vaccineService.getById(vaccine.getId()));
                        personVaccineToDo.add(newPV);
                    }
                }
            }
        }
        return personVaccineToDo;
    }

    private PersonVaccine createNewPersonVaccine(Person person, Vaccine vaccine) {
        PersonVaccine newPV = new PersonVaccine();
        newPV.setPerson(person);
        newPV.setVaccine(vaccine);
        newPV.setMade(false);
        newPV.setVaccinationDate(Date.valueOf(LocalDate.now()));
        return newPV;
    }
}
