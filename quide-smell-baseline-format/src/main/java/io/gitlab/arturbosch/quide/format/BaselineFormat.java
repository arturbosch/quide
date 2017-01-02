package io.gitlab.arturbosch.quide.format;

import io.gitlab.arturbosch.quide.format.internal.Validate;
import io.gitlab.arturbosch.quide.format.xml.SmellBaseline;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BaselineFormat {

	private final Unmarshaller unmarshaller;
	private final Marshaller marshaller;

	public BaselineFormat() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(SmellBaseline.class);
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		unmarshaller = context.createUnmarshaller();
	}

	public SmellBaseline read(Path path) throws JAXBException {
		Validate.notNull(path);
		try (InputStream is = Files.newInputStream(path)) {
			return (SmellBaseline) unmarshaller.unmarshal(is);
		} catch (IOException e) {
			throw new JAXBException(e);
		}
	}

	public void write(SmellBaseline baseline, Path path) throws JAXBException {
		Validate.notNull(baseline);
		Validate.notNull(path);
		marshaller.marshal(baseline, path.toFile());
	}
}