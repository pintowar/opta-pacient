package com.github.pintowar.app.util;

import com.github.pintowar.app.model.PatientAdmissionSchedule;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import lombok.val;

import java.net.URL;

public class PasReader {

    public static PatientAdmissionSchedule readFromXml(URL url) {
        val xStream = new XStream();
        xStream.setMode(XStream.ID_REFERENCES);
        XStream.setupDefaultSecurity(xStream);
        xStream.processAnnotations(PatientAdmissionSchedule.class);

        xStream.addPermission(new AnyTypePermission());
        return (PatientAdmissionSchedule) xStream.fromXML(url, new PatientAdmissionSchedule());
    }
}
