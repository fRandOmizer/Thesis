/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.processing.comparison.surfaceComparison;

import cz.fidentis.comparison.ICPmetric;
import cz.fidentis.comparison.icp.ICPTransformation;
import cz.fidentis.comparison.icp.Icp;
import cz.fidentis.comparison.icp.KdTreeFaces;
import cz.fidentis.comparison.kdTree.KDTreeIndexed;
import cz.fidentis.comparison.kdTree.KdTree;
import cz.fidentis.controller.BatchComparison;
import cz.fidentis.model.Model;
import cz.fidentis.processing.fileUtils.ProcessingFileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.vecmath.Vector3f;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;

/**
 * Class designed for multi-threading of batch regisration.
 *
 * @author Zuzana Ferkova
 */
public class BatchProcessingCallable {

    private final Model compF;
    private final List<Vector3f> samples;
    private final Model template;
    private final KdTree templateTree;
    private final int batchIteration;
    private final int currentModelNumber;
    private final File saveTo;
    private final float error;
    private final int iterations;
    private final boolean scale;
    private final boolean align;
    private final ICPmetric metric;
    private final BatchComparison data;

    /**
     * Data needed to perform call() method
     *
     * @param compF - face to align to template and compute new average face
     * from
     * @param samples
     * @param template - template to create new avreage face from
     * @param templateTree - template respresented as KdTree
     * @param error - error for ICP computation denoting when no more alignment
     * is necessary
     * @param iterations - number of iterations for ICP
     * @param scale - whether to use scale during ICP
     * @param saveTo - address to folder to disk where to save aligned compFs
     * @param currentModelNumber - current number of model in list of all models
     * (to generate appropriate name when saving to disk)
     * @param batchIteration - number of current batch iteration
     * @param align - whether to use alignment or just compute average face
     * transformations
     * @param metric - ICP metric used for alignment
     */
    public BatchProcessingCallable(Model compF, List<Vector3f> samples, Model template, KdTree templateTree, float error, int iterations, boolean scale, File saveTo, int currentModelNumber, int batchIteration, boolean align,
            ICPmetric metric, BatchComparison data) {
        this.compF = compF;
        this.samples = samples;
        this.templateTree = templateTree;
        this.template = template;
        this.saveTo = saveTo;
        this.error = error;
        this.iterations = iterations;
        this.scale = scale;
        this.batchIteration = batchIteration;
        this.currentModelNumber = currentModelNumber;
        this.align = align;
        this.metric = metric;
        this.data = data;
    }

    /**
     * Performs ICP of compF to template, saves aligned compF to tempory folder
     * on disk and computes parameters for creating new average face.
     *
     * @return parameters for new average face
     * @throws Exception
     */
 
    public List<Vector3f> call() {
        ProgressHandle p = null;

        KdTree computeMorph;
        Vector3f near;
        List<Vector3f> trans = new ArrayList<Vector3f>(template.getVerts().size());

        if (align) {
            p = ProgressHandleFactory.createHandle("Aligning face " + (currentModelNumber + 1) + ", Batch Iteration " + (batchIteration + 1));
            p.start();

            try {
                List<ICPTransformation> tran = Icp.instance().icp(templateTree, compF.getVerts(), samples, error, iterations, scale);
                
                if(data != null){                
                    data.addTrans(tran, currentModelNumber);
                }                
                
                ProcessingFileUtils.instance().saveModelToTMP(compF, saveTo, batchIteration + 1, currentModelNumber, Boolean.FALSE);
            } catch (Exception ex) {
                p.finish();
                Exceptions.printStackTrace(ex);
            }
        }

        if (metric == ICPmetric.VERTEX_TO_VERTEX) {
            computeMorph = new KDTreeIndexed(compF.getVerts());
        } else {
            computeMorph = new KdTreeFaces(compF.getVerts(), compF.getFaces());
        }

        for (Vector3f point : template.getVerts()) {
            near = computeMorph.nearestNeighbour(point);
            trans.add(new Vector3f(near.x - point.x, near.y - point.y, near.z - point.z));
        }

        if (p != null) {
            p.finish();
        }

        return trans;
    }

}
