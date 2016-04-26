class SortedProperties extends Properties {
    Comparator<Object> comparator

    @Override
    public Enumeration<Object> keys() {
        return Collections.enumeration(keySet())
    }

    @Override
    public Set<Object> keySet() {
        Set<Object> keySet = new TreeSet<Object>(comparator)
        keySet.addAll(super.keySet())
        return Collections.unmodifiableSet(keySet)
    }


    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        TreeMap<Object, Object> map = new TreeMap<Object, Object>(comparator)
        for (Map.Entry<Object, Object> entry : super.entrySet()) {
            map.put(entry.getKey(), entry.getValue())
        }
        return Collections.unmodifiableSet(map.entrySet())
    }
}

Set properties = new HashSet()

def removeNewLineAndTabCharacters = { text ->
    text = text.replaceAll("\\n", " ")
    text = text.replaceAll("\\t", " ")
    return text;
}

def scrape = { pluginXmlFile ->
    def slurper = new XmlSlurper().parse(pluginXmlFile)
    def pluginName = slurper.header.identifier.@name.text()

    properties.add(pluginName)

    def pluginDescription = slurper.header.description.text().trim()
    properties.add(pluginDescription)

    def fullTag = slurper.header.tag.text()

    fullTag.split("/").each { tag ->
        properties.add(tag)
    }

    slurper."step-type".each { step ->
        def stepName = step.@name.text()
        properties.add(stepName)

        def stepDescription = step.description.text().trim()
        properties.add(stepDescription)

        def propertiesXml = step.properties;
        propertiesXml."property".each { propXml ->
            def propLabel = propXml."property-ui".@label.text()
            def propDesc = propXml."property-ui".@description.text()
            propXml."value".each { valueXml ->
                def propValue = valueXml.@label.text()
                properties.add(propValue)
            }
            properties.add(propLabel)
            properties.add(propDesc)
        }
    }

    slurper."property-group".each { propGroup ->
        def propGroupName = propGroup.@name.text()
        properties.add(propGroupName)

        propGroup."property".each { propXml ->
            def propLabel = propXml."property-ui".@label.text()
            def propDesc = propXml."property-ui".@description.text()
            propXml."value".each { valueXml ->
                def propValue = valueXml.@label.text()
                properties.add(propValue)
            }
            properties.add(propLabel)
            properties.add(propDesc)
        }
    }
}

println "Scraping plugin.xml for translatable strings..."
File pluginXmlFile = new File("src/main/zip/plugin.xml")
scrape(pluginXmlFile)

List propertyList = new ArrayList(properties)
Collections.sort(propertyList)

Properties translationProperties = new SortedProperties()
translationProperties.setComparator(
        new Comparator<Object>() {
            public int compare(Object obj1, Object obj2) {
                String prop1 = String.valueOf(obj1)
                String prop2 = String.valueOf(obj2)
                if (prop1.equalsIgnoreCase(prop2)) {
                    return prop1.compareTo(prop2)
                }
                else {
                    return prop1.compareToIgnoreCase(prop2)
                }
            }
        }
)
for (def prop : propertyList) {
    if (prop?.trim()?.length() > 0) {
        prop = removeNewLineAndTabCharacters(prop)
        prop = prop.replaceAll(" +", " ")
        translationProperties.put(prop, prop)
    }
}

println "Writing out the sorted properties..."
String localeFile = args[0]
File outputFile = new File(localeFile)
OutputStream os = new ByteArrayOutputStream()
translationProperties.store(os, null)

// Output all lines except for the date comment included by the Properties class
def lines = os.toString("UTF-8").readLines()
def outputLines = ["#NLS_ENCODING=UTF-8", "#NLS_MESSAGEFORMAT_NONE"]
outputLines.addAll(lines[1..lines.size() - 1])
outputLines << ""
outputFile.write(outputLines.join("\r\n"))
os.close()
