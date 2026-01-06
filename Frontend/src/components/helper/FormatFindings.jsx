const FormatFindings = ({ findingsText = "" }) => {
  if (!findingsText) {
    return <p className="italic text-gray-500">No findings available</p>;
  }

  // Split into lines
  const lines = findingsText.split("\n").map(line => line.trim()).filter(Boolean);

  let currentSection = null;
  const sections = [];

  lines.forEach(line => {
    if (line === "IMPRESSION" || line==="FINDINGS" || (line === line.toUpperCase() && line.endsWith(":"))) {
      // Start a new section
      currentSection = { heading: line.replace(":", ""), bullets: [] };
      sections.push(currentSection);
    } else if (line.startsWith("-")) {
      // Bullet point
      currentSection?.bullets.push(line.replace("-", "").trim());
    } else {
      // Plain text under heading
      currentSection?.bullets.push(line.trim());
    }
  });

  return (
    <div>
      {sections.map((section, idx) => (
        <div key={idx} className="mb-4">
          <h3
            className={
              section.heading === "IMPRESSION" || section.heading==="FINDINGS"
                ? "font-bold text-green-700 border p-2 rounded"
                : "font-semibold text-blue-700"
            }
          >
            {section.heading}
          </h3>
          <ul className="list-disc list-inside space-y-1 pl-4">
            {section.bullets.map((bullet, i) => (
              <li key={i}>{bullet}</li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
};

export default FormatFindings;
