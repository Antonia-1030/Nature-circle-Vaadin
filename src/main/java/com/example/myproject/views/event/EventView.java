package com.example.myproject.views.event;

import com.example.myproject.data.Event;
import com.example.myproject.data.SampleBook;
import com.example.myproject.services.EventService;
import com.example.myproject.data.EventRepository;
import com.example.myproject.services.SampleBookService;
import com.example.myproject.views.MainLayout;
import com.example.myproject.views.products.ProductsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Base64;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Event")
@Route(value = "event-view/:eventID?/:action?(edit)", layout = MainLayout.class)
public class EventView extends Div implements BeforeEnterObserver {

    private final String EVENT_ID = "eventID";
    private final String EVENT_EDIT_ROUTE_TEMPLATE = "event-view/%s/edit";
    private final Grid<Event> grid = new Grid<>(Event.class, false);
    private TextField name;
    private DatePicker date;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<Event> binder;

    private Event event;

    private final EventService eventService;

    public EventView(EventService eventService) {
        this.eventService = eventService;
        addClassNames("event-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid

        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("date").setAutoWidth(true);
        grid.setItems(query -> eventService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(EVENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProductsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Event.class);

        // Bind fields. This is where you'd define e.g. validation rules
        //binder.forField(pages).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("pages");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        delete.addClickListener(e -> {
            if (this.event != null) {
                eventService.delete(this.event.getId());
                refreshGrid();
            } else {
                Notification.show("No product selected for deletion", 3000, Notification.Position.MIDDLE);
            }
        });

        save.addClickListener(e -> {
            try {
                if (this.event == null) {
                    this.event = new Event();
                }
                binder.writeBean(this.event);
                eventService.update(this.event);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ProductsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> eventID = event.getRouteParameters().get(EVENT_ID).map(Long::parseLong);
        if (eventID.isPresent()) {
            Optional<Event> eventFromBackend = eventService.get(eventID.get());
            if (eventFromBackend.isPresent()) {
                populateForm(eventFromBackend.get());
            } else {
                Notification.show(String.format("The requested sampleBook was not found, ID = %s", eventID.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProductsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("Name");
        //author = new TextField("Author");
        date = new DatePicker("Date");
        //pages = new TextField("Pages");
        formLayout.add( name, date);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel,delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Event value) {
        this.event = value;
        binder.readBean(this.event);
    }
}
