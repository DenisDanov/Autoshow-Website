const fs = require('fs');
const csv = require('csv-parser');

const csvFilePath = 'car_query_makes.csv';
const jsonFilePath = 'jsonfile.json';

const csvData = [];

fs.createReadStream(csvFilePath)
  .pipe(csv())
  .on('data', (row) => {
    csvData.push(row);
  })
  .on('end', () => {
    fs.writeFile(jsonFilePath, JSON.stringify(csvData, null, 2), 'utf8', (err) => {
      if (err) {
        console.error('Error writing JSON file:', err);
      } else {
        console.log(`Conversion completed. JSON file saved at: ${jsonFilePath}`);
      }
    });
  });
