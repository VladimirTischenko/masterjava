package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;

public class MainXml {
    public static void main(String[] args) throws IOException, JAXBException {
        JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
        jaxbParser.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload = jaxbParser.unmarshal(
                Resources.getResource("payload.xml").openStream());
        List<Project> projects = payload.getProjects().getProject();
        String projectName = args[0];
        Project project = projects.stream().filter(p -> projectName.equals(p.getTitle())).findFirst().orElseThrow(() -> new RuntimeException("нет проекта " + projectName));
        Set<UserType> users = getAllParticipants(project);
        users.forEach(userType -> System.out.println(userType.getId()));
    }

    public static Set<UserType> getAllParticipants(Project project) {
        Set<UserType> users = new TreeSet<>(Comparator.comparing(UserType::getId));
        List<Group> groups = project.getGroup();
        groups.forEach(group -> {
            List<JAXBElement<Object>> elements = group.getUser();
            elements.forEach(element -> {
                UserType user = (UserType) element.getValue();
                users.add(user);
            });
        });
        return users;
    }
}
